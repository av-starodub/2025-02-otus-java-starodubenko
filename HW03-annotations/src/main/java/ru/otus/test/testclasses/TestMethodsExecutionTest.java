package ru.otus.test.testclasses;

import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

public class TestMethodsExecutionTest {
    @Test
    public void checkFailedTestMethod() {
        assertTrue(false);
    }

    @Test
    public void checkPassedTestMethod() {
        assertTrue(true);
    }
}
