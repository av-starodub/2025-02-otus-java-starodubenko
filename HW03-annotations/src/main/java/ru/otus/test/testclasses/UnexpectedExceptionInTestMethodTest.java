package ru.otus.test.testclasses;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.*;

public class UnexpectedExceptionInTestMethodTest {
    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
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
