package ru.otus.simplejunit.logger;

import java.util.Arrays;

public enum TestResultLogger {
    PASSED("PASSED", 0),
    FAILED("FAILED", 0),
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

    public static void resetLogger() {
        Arrays.stream(values()).forEach(event -> event.numberOfTests = 0);
    }

    @Override
    public String toString() {
        return String.format("%s - %d", event, numberOfTests);
    }
}
