package dz1;

public class TestExceptions {

    public static class BadTestClassError extends Error {
        public BadTestClassError(String message) {
            super(message);
        }

        public BadTestClassError(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class AssertionFailedError extends AssertionError {
        public AssertionFailedError(String message) {
            super(message);
        }

        public AssertionFailedError(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
