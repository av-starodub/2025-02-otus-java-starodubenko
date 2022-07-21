package ru.otus.test.testclasses;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

public class BeforeMethodWithExceptionTest {
    @Before
    public void setUp() {
        throw new RuntimeException("Exception in method annotated with @Before.");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void checkSkippedTestMethod() {
        assertTrue(true);
    }
}
