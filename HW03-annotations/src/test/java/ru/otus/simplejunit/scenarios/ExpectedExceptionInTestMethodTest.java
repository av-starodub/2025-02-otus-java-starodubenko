package ru.otus.simplejunit.scenarios;

import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertThrow;

public class ExpectedExceptionInTestMethodTest {
    @Test
    public void checkPassWithExpectedException() {
        try {
            throw new RuntimeException();
        } catch (Exception e) {
            assertThrow(RuntimeException.class, e.getClass());
        }
    }
}
