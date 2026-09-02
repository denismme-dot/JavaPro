package dz1;

import java.util.*;

public class MainRunner {
    public static void main(String[] args) {
        try {
            System.out.println("<--- Start tests --->");
            Map<TestResult, List<TestDetails>> results = TestRunner.runTests(CalculatorTest.class);

            System.out.println("\n<--- MainRunner Results --->");

            System.out.println("\n Good tests:");
            for (TestDetails report : results.get(TestResult.SUCCESS)) {
                System.out.printf("  - %s (priority: %d)%n",
                        report.getTestName(), report.getPriority());
            }

            System.out.println("\n Test failed (AssertionFailedError):");
            for (TestDetails report : results.get(TestResult.FAILED)) {
                System.out.printf("  - %s (priority: %d)%n",
                        report.getTestName(), report.getPriority());
                report.getError().ifPresent(e ->
                        System.out.println("    Error message: " + e.getMessage()));
            }

            System.out.println("\n️ Error tests:");
            for (TestDetails report : results.get(TestResult.ERROR)) {
                System.out.printf("  - %s (priority: %d)%n",
                        report.getTestName(), report.getPriority());
                report.getError().ifPresent(e ->
                        System.out.println("    Error message: " + e.getMessage()));
            }

            System.out.println("\n️ Skipped tests:");
            for (TestDetails report : results.get(TestResult.SKIPPED)) {
                System.out.printf("  - %s (priority: %d, ignored: %b)%n",
                        report.getTestName(), report.getPriority(), report.isIgnored());
                report.getError().ifPresent(e ->
                        System.out.println("    Reason: " + e.getMessage()));
            }

            int total = results.values().stream().mapToInt(List::size).sum();
            System.out.println("\n<--- Stats --->");
            System.out.println("Total test count: " + total);
            System.out.println(" Good: " + results.get(TestResult.SUCCESS).size());
            System.out.println(" Failed: " + results.get(TestResult.FAILED).size());
            System.out.println(" Errors: " + results.get(TestResult.ERROR).size());
            System.out.println("️ Skipped: " + results.get(TestResult.SKIPPED).size());

            System.out.println("\n<--- Exec order --->");
            System.out.println("Test sorted by: Priority (from 10 to 0), Order (from 1 to 10), Name (by alphabet)");

        } catch (TestExceptions.BadTestClassError e) {
            System.err.println("Exception in test class: " + e.getMessage());
            e.printStackTrace();
        }
    }
}