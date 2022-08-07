package ru.otus.simplejunit.scenarios;

import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertTrue;
/**
 * The class implements normal operation scenario.
 */
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
