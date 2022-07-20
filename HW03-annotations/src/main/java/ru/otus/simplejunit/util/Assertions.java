package ru.otus.simplejunit.util;

public class Assertions {
    private Assertions() {
    }

    public static void assertTrue(boolean expression) {
    }

    public static void assertThrow(Class<? extends Exception> expected, Class<? extends Exception> actual) {
        assertTrue(expected.equals(actual));
    }
}
