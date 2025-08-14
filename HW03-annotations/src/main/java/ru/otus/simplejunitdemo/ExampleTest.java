package ru.otus.simplejunitdemo;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

public class ExampleTest {
    @Before
    public void setUp() {}

    @After
    public void tearDown() {}

    @Test
    public void anyTest1() {
        assertTrue(false);
    }

    @Test
    public void anyTest2() {
        assertTrue(true);
    }
}
