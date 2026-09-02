package dz1;

import static dz1.MyAnnotations.*;

public class CalculatorTest {

    @BeforeAll
    static void init() {
        System.out.println("Begin of calc tests");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("End of calc tests");
    }

    @BeforeEach
    void setup() {
        System.out.println("  -> ");
    }

    @AfterEach
    void teardown() {
        System.out.println("  <- ");
    }

    @Test(name = "1) Check ADD", priority = 10)
    @Order(1)
    void testSum() {
        System.out.println("    Check ADD");
        int a = 15;
        int b = 13;
        int expected = 18;
        System.out.println("    " + a + " + " + b + " = " + expected);
    }

    @Test(name = "2) Check SUB", priority = 8)
    @Order(2)
    public void testSubtract() {
        System.out.println("    Check SUB");
        int a = 110;
        int b = 24;
        int expected = 86;
        System.out.println("    " + a + " - " + b + " = " + expected);
    }

    @Test(name = "3) Check MUL", priority = 5)
    @Order(1)
    void testMultiply() {
        System.out.println("    Check MUL");
        int a = 13;
        int b = 13;
        int expected = 169;
        System.out.println("    " + a + " * " + b + " = " + expected);
    }

    @Test(name = "4) Check DIV", priority = 5)
    @Order(2)
    void testDivide() {
        System.out.println("    Check DIV");
        int a = 18;
        int b = 9;
        int expected = 2;
        System.out.println("    " + a + " / " + b + " = " + expected);
    }

    @Test(name = "5) Check XOR", priority = 4)
    @Order(3)
    void testXOR() {
        System.out.println("    Check XOR");
        int a = 0xA;
        int b = 0xB;
        int expected = 0x1;
        System.out.println("    " + a + " / " + b + " = " + expected);
    }

    @Test(name = "Ignored test 1", priority = 7)
    @Ignore
    void testIgnored1() {
        System.out.println("    This is ignored test 1");
    }

    @Test(name = "Ignored test 2", priority = 8)
    @Ignore
    void testIgnored2() {
        System.out.println("    This is ignored test 2");
    }

    @Test(name = "Test with error 1", priority = 9)
    void testWithError1() {
        System.out.println("    This is test with error 1");
        throw new TestExceptions.AssertionFailedError("Expected 2, get 1");
    }

    @Test(name = "Test with error 2", priority = 10)
    void testWithError2() {
        System.out.println("    This is test with error");
        throw new TestExceptions.AssertionFailedError("Expected 1, get 2");
    }

    @Test(name = "Test with exception 1", priority = 6)
    void testWithException1() {
        System.out.println("    Test with exception 1");
        throw new RuntimeException("WOW! EXCEPTION 1");
    }

    @Test(name = "Test with exception 2", priority = 7)
    void testWithException2() {
        System.out.println("    Test with exception 2");
        throw new RuntimeException("WOW! EXCEPTION 2");
    }

}