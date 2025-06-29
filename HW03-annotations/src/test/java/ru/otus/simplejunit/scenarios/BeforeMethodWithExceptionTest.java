package ru.otus.simplejunit.scenarios;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;
import ru.otus.simplejunit.scenarios.util.CallMethodWriter;

/**
 * The class implements the test scenario when an exception occurs in method annotated with @Before.
 */
public class BeforeMethodWithExceptionTest {
    @Before
    public void setUp() {
        throw new RuntimeException();
    }

    @After
    public void tearDown() {}

    @Test
    public void firstTest() {
        CallMethodWriter.call("firstTest");
        assertTrue(true);
    }

    @Test
    public void secondTest() {
        CallMethodWriter.call("secondTest");
        assertTrue(true);
    }
}
