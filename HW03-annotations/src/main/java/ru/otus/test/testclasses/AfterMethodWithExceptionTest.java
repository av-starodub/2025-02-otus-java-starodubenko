package ru.otus.test.testclasses;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

/**
 * Only one method annotated with @Test should be called.
 */
public class AfterMethodWithExceptionTest {
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        throw new RuntimeException("Exception in method annotated with @After");
    }

    @Test
    public void firstTest() {
        assertTrue(true);
    }

    @Test
    public void secondTest() {
        assertTrue(true);
    }
}
