package dz1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import static dz1.MyAnnotations.*;

public class TestRunner {

    public static Map<TestResult, List<TestDetails>> runTests(Class<?> c) {
        Map<TestResult, List<TestDetails>> results = initResults();

        Object testInstance = checkIfCanCreateInstance(c);
        Method[] allMethods = c.getDeclaredMethods();
        TestMethodsHolder holder = gatherMethods(allMethods, c);
        if (holder.tests.isEmpty()) {
            return results;
        }
        sortTests(holder.tests);
        if (!runBeforeAll(holder.beforeAll, results, holder.tests)) {
            return results;
        }
        runAllTests(c, testInstance, holder, results);
        runAfterAll(holder.afterAll, results);
        return results;
    }

    private static Map<TestResult, List<TestDetails>> initResults() {
        Map<TestResult, List<TestDetails>> results = new HashMap<>();
        for (TestResult status : TestResult.values()) {
            results.put(status, new ArrayList<>());
        }
        return results;
    }

    private static Object checkIfCanCreateInstance(Class<?> testClass) {
        try {
            return testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new TestExceptions.BadTestClassError(
                    "Can't create object of class " + testClass.getName(), e);
        }
    }

    private static TestMethodsHolder gatherMethods(Method[] methods, Class<?> testClass) {
        TestMethodsHolder holder = new TestMethodsHolder();

        for (Method m : methods) {
            if (m.isAnnotationPresent(BeforeAll.class)) {
                checkStatic(m, "BeforeAll", testClass);
                holder.beforeAll = m;
            }

            if (m.isAnnotationPresent(AfterAll.class)) {
                checkStatic(m, "AfterAll", testClass);
                holder.afterAll = m;
            }

            if (m.isAnnotationPresent(MyAnnotations.Test.class)) {
                checkNotStatic(m, "Test", testClass);
                TestMethodData testData = buildTestData(m, testClass);
                holder.tests.add(testData);
            }

            if (m.isAnnotationPresent(BeforeEach.class)) {
                checkNotStatic(m, "BeforeEach", testClass);
                holder.beforeEach.add(m);
            }

            if (m.isAnnotationPresent(AfterEach.class)) {
                checkNotStatic(m, "AfterEach", testClass);
                holder.afterEach.add(m);
            }
        }

        return holder;
    }

    private static void checkStatic(Method m, String annotation, Class<?> testClass) {
        if (!Modifier.isStatic(m.getModifiers())) {
            throw new TestExceptions.BadTestClassError(
                    annotation + " must be static: " + m.getName() + " in class " + testClass.getName()
            );
        }
    }

    private static void checkNotStatic(Method m, String annotation, Class<?> testClass) {
        if (Modifier.isStatic(m.getModifiers())) {
            throw new TestExceptions.BadTestClassError(
                    annotation + " can't by static : " + m.getName() + " in class " + testClass.getName()
            );
        }
    }

    private static TestMethodData buildTestData(Method m, Class<?> testClass) {
        MyAnnotations.Test testAnn = m.getAnnotation(MyAnnotations.Test.class);
        String name = testAnn.name().isEmpty() ? m.getName() : testAnn.name();
        int priority = testAnn.priority();

        if (priority < 0 || priority > 10) {
            throw new TestExceptions.BadTestClassError(
                    "Priority must be from 0 to 10 in method " + m.getName() + " of class " + testClass.getName()
            );
        }

        boolean ignored = m.isAnnotationPresent(Ignore.class);

        int order = 5;
        if (m.isAnnotationPresent(Order.class)) {
            Order orderAnn = m.getAnnotation(Order.class);
            order = orderAnn.value();
            if (order < 1 || order > 10) {
                throw new TestExceptions.BadTestClassError(
                        "Order must be from 1 to 10 in method " + m.getName() + " of class " + testClass.getName()
                );
            }
        }

        return new TestMethodData(m, name, priority, ignored, order);
    }

    private static void sortTests(List<TestMethodData> tests) {
        tests.sort((t1, t2) -> {
            if (t1.priority() != t2.priority()) {
                return Integer.compare(t2.priority(), t1.priority());
            }
            if (t1.order() != t2.order()) {
                return Integer.compare(t1.order(), t2.order());
            }
            return t1.name().compareTo(t2.name());
        });
    }

    private static Object createInstance(Class<?> testClass) {
        try {
            return testClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new TestExceptions.BadTestClassError(
                    "Can't create instance of class " + testClass.getName(), e);
        }
    }

    private static boolean runBeforeAll(Method beforeAll,
                                        Map<TestResult, List<TestDetails>> results,
                                        List<TestMethodData> tests) {
        if (beforeAll == null) {
            return true;
        }

        try {
            beforeAll.setAccessible(true);
            beforeAll.invoke(null);
            return true;
        } catch (Exception e) {
            for (TestMethodData test : tests) {
                results.get(TestResult.SKIPPED).add(
                        new TestDetails(TestResult.SKIPPED, test.name(),
                                new IllegalStateException("BeforeAll failed", e),
                                test.priority(), test.ignored())
                );
            }
            return false;
        }
    }

    private static void runAllTests(Class<?> testClass, Object instance,
                                    TestMethodsHolder holder,
                                    Map<TestResult, List<TestDetails>> results) {
        for (TestMethodData testData : holder.tests) {
            if (testData.ignored()) {
                results.get(TestResult.SKIPPED).add(
                        new TestDetails(TestResult.SKIPPED, testData.name(),
                                new IllegalStateException("Test checked as Ignore"),
                                testData.priority(), true)
                );
                continue;
            }
            Object freshInstance = createInstance(testClass);
            if (!runBeforeEach(holder.beforeEach, freshInstance, testData, results)) {
                continue;
            }
            TestRunResult runResult = runTest(testData, freshInstance);
            runAfterEach(holder.afterEach, freshInstance, testData, results);
            results.get(runResult.status).add(
                    new TestDetails(runResult.status, testData.name(),
                            runResult.error, testData.priority(), testData.ignored())
            );
        }
    }

    private static boolean runBeforeEach(List<Method> beforeEachMethods,
                                         Object instance,
                                         TestMethodData testData,
                                         Map<TestResult, List<TestDetails>> results) {
        for (Method m : beforeEachMethods) {
            try {
                m.setAccessible(true);
                m.invoke(instance);
            } catch (Exception e) {
                Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
                results.get(TestResult.ERROR).add(
                        new TestDetails(TestResult.ERROR, testData.name(),
                                new IllegalStateException("BeforeEach failed: " + cause.getMessage(), cause),
                                testData.priority(), false)
                );
                return false;
            }
        }
        return true;
    }

    private static TestRunResult runTest(TestMethodData testData, Object instance) {
        TestResult status = TestResult.SUCCESS;
        Throwable error = null;

        try {
            Method m = testData.method();
            m.setAccessible(true);
            m.invoke(instance);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            error = cause;
            if (cause instanceof TestExceptions.AssertionFailedError) {
                status = TestResult.FAILED;
            } else {
                status = TestResult.ERROR;
            }
        } catch (Exception e) {
            status = TestResult.ERROR;
            error = e;
        }

        return new TestRunResult(status, error);
    }

    private static void runAfterEach(List<Method> afterEachMethods,
                                     Object instance,
                                     TestMethodData testData,
                                     Map<TestResult, List<TestDetails>> results) {
        for (Method m : afterEachMethods) {
            try {
                m.setAccessible(true);
                m.invoke(instance);
            } catch (Exception e) {
                Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
                results.get(TestResult.ERROR).add(
                        new TestDetails(TestResult.ERROR,
                                "AfterEach for " + testData.name(),
                                new IllegalStateException("AfterEach failed: " + cause.getMessage(), cause),
                                0, false)
                );
            }
        }
    }

    private static void runAfterAll(Method afterAll,
                                    Map<TestResult, List<TestDetails>> results) {
        if (afterAll == null) {
            return;
        }

        try {
            afterAll.setAccessible(true);
            afterAll.invoke(null);
        } catch (Exception e) {
            Throwable cause = e instanceof InvocationTargetException ? e.getCause() : e;
            results.get(TestResult.ERROR).add(
                    new TestDetails(TestResult.ERROR, "AfterAll", cause, 0, false)
            );
        }
    }

    private static class TestMethodsHolder {
        List<TestMethodData> tests = new ArrayList<>();
        Method beforeAll = null;
        Method afterAll = null;
        List<Method> beforeEach = new ArrayList<>();
        List<Method> afterEach = new ArrayList<>();
    }

    private static class TestRunResult {
        final TestResult status;
        final Throwable error;

        TestRunResult(TestResult status, Throwable error) {
            this.status = status;
            this.error = error;
        }
    }
}