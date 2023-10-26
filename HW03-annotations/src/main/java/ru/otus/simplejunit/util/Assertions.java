package ru.otus.simplejunit.util;

<<<<<<< HEAD
import ru.otus.simplejunit.exceptions.AssertionError;

public final class Assertions {
=======
public class Assertions {
>>>>>>> 6799d7a (Create test classes for testing the SimpleJunit.)
    private Assertions() {
    }

    public static void assertTrue(boolean expression) {
<<<<<<< HEAD
        if (!expression) {
            throw new AssertionError("expected: true, actual: false.");
        }
=======
>>>>>>> 6799d7a (Create test classes for testing the SimpleJunit.)
    }

    public static void assertThrow(Class<? extends Exception> expected, Class<? extends Exception> actual) {
        assertTrue(expected.equals(actual));
    }
}
