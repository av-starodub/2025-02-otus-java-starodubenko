package ru.otus.simplejunit.scenarios;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

import ru.otus.simplejunit.annotations.*;
import ru.otus.simplejunit.scenarios.util.CallMethodWriter;

/**
 * The class implements the test scenario with several preparatory methods.
 */
public class SeveralPreparatoryMethodsExecutionTest {

    @Before
    public void setUp1() {
        CallMethodWriter.call("setUp1");
    }

    @Before
    public void setUp2() {
        CallMethodWriter.call("setUp2");
    }

    @After
    public void tearDown1() {
        CallMethodWriter.call("tearDown1");
    }

    @After
    public void tearDown2() {
        CallMethodWriter.call("tearDown2");
    }

    @Test
    public void anyTest() {
        CallMethodWriter.call("anyTest");
        assertTrue(true);
    }
}
