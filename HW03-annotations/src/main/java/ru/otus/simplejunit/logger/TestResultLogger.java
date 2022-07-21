package ru.otus.simplejunit.logger;

public enum TestResultLogger {
    PASSED("PASSED", 0),
    FAIL("FAIL", 0),
    SKIPPED("SKIPPED", 0);
    private final String event;
    private int numberOfTests;

    TestResultLogger(String event, int numberOfTests) {
        this.event = event;
        this.numberOfTests = numberOfTests;
    }

    public void addEvent() {
        numberOfTests++;
    }

    @Override
    public String toString() {
        return String.format("%s - %d", event, numberOfTests);
    }
}
