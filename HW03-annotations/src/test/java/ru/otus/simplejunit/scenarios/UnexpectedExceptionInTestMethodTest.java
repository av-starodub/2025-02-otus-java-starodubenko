package ru.otus.simplejunit.scenarios;

import static ru.otus.simplejunit.util.Assertions.*;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

/**
 * The class implements the test scenario with unexpected exception in method annotated with @Test.
 */
public class UnexpectedExceptionInTestMethodTest {
    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    @Test
    public void executeAnyWay() {
        assertTrue(true);
    }

    @Test
    public void throwUnexpectedException() {
        throw new RuntimeException("Unexpected exception in method annotated with @Test.");
    }

    @Test
    public void executeAnyWayToo() {
        assertTrue(true);
    }
}
