package dz1;

import java.util.Optional;

public class TestDetails {
    private final TestResult status;
    private final String testName;
    private final Throwable error;
    private final int priority;
    private final boolean ignored;

    public TestDetails(TestResult status, String testName, Throwable error, int priority, boolean ignored) {
        this.status = status;
        this.testName = testName;
        this.error = error;
        this.priority = priority;
        this.ignored = ignored;
    }

    public TestResult getStatus() {
        return status;
    }

    public String getTestName() {
        return testName;
    }

    public Optional<Throwable> getError() {
        return Optional.ofNullable(error);
    }

    public int getPriority() {
        return priority;
    }

    public boolean isIgnored() {
        return ignored;
    }

    @Override
    public String toString() {
        return String.format("TestReport {name='%s', status=%s, priority=%d, ignored=%b%s}",
                testName, status, priority, ignored, error != null ? ", error=" + error.getMessage() : "");
    }
}