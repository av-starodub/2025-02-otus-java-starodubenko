package ru.otus.simplejunit.scenarios;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;
import ru.otus.simplejunit.scenarios.util.CallMethodWriter;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

/**
 * The class implements the test scenario when an exception occurs in method annotated with @After.
 */
public class AfterMethodWithExceptionTest {

    public static String call(String name) {
        return name;
    }
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
        throw new RuntimeException("Exception in method annotated with @After");
    }

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
