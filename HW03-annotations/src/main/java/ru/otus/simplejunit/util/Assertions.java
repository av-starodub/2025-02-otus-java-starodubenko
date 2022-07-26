package ru.otus.simplejunit.util;

import ru.otus.simplejunit.exceptions.TestException;

public final class Assertions {
    private Assertions() {
    }

    public static void assertTrue(boolean expression) {
        if (!expression) {
            throw new TestException("TEST FAILED");
        }
    }

    public static void assertThrow(Class<? extends Exception> expected, Class<? extends Exception> actual) {
        assertTrue(expected.equals(actual));
    }
}
