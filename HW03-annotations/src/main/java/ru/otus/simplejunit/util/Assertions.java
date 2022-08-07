package ru.otus.simplejunit.util;

import ru.otus.simplejunit.exceptions.AssertionError;

public final class Assertions {
    private Assertions() {
    }

    public static void assertTrue(boolean expression) {
        if (!expression) {
            throw new AssertionError("expected: true, actual: false.");
        }
    }

    public static void assertThrow(Class<? extends Exception> expected, Class<? extends Exception> actual) {
        assertTrue(expected.equals(actual));
    }
}
