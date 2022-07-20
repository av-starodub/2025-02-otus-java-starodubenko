package ru.otus.test.testclasses;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

public class AfterMethodWithExceptionTest {
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        throw new RuntimeException("After method exception");
    }

    @Test
    public void executeBeforeException() {
        assertTrue(true);
    }

    @Test
    public void doNotExecuteAfterException() {
        assertTrue(true);
    }
}
