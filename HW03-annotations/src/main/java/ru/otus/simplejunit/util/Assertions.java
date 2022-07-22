package ru.otus.simplejunit.util;

import ru.otus.simplejunit.exceptions.TestException;

import static ru.otus.simplejunit.logger.TestResultLogger.PASSED;

public final class Assertions {
    private Assertions() {
    }

    public static void assertTrue(boolean expression) {
        if (!expression) {
            throw new TestException("TEST FAILED");
        }
        PASSED.addEvent();
    }

    public static void assertThrow(Class<? extends Exception> expected, Class<? extends Exception> actual) {
        assertTrue(expected.equals(actual));
    }
}
