package ru.otus.simplejunit.cash;

public class ResultContainer {
    private int passed;
    private int failed;
    private int skipped;

    public void addPassed() {
        passed++;
    }

    public void addFailed() {
        failed++;
    }

    public void addSkipped() {
        skipped++;
    }

    @Override
    public String toString() {
        return String.format("TEST RESULTS: PASSED - %d, FAILED - %d, SKIPPED - %d\n", passed, failed, skipped);
    }
}
