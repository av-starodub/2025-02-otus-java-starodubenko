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
    public void throwUnexpectedException() {
        throw new RuntimeException("Test method unexpected exception");
    }

    @Test
    public void executeAfterUnexpectedException() {
        assertTrue(true);
    }
}
