package ru.otus.simplejunit.scenarios;

import static ru.otus.simplejunit.util.Assertions.assertThrow;

import ru.otus.simplejunit.annotations.Test;

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
