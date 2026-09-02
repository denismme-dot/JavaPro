package dz1;

public enum TestResult {
    SUCCESS,         // Good test
    FAILED,       // Failed with AssertionFailedError
    ERROR,      // Failed with another exc
    SKIPPED        // Skipped
}
