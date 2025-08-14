package ru.otus.simplejunit.demo;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

public class BeforeMethodWithExceptionTest {
    @Before
    public void setUp() {
        throw new RuntimeException("Before method exception");
    }

    @After
    public void tearDown() {
        assertTrue(true);
    }

    @Test
    public void checkSkippedTestMethod() {
        assertTrue(true);
    }
}
