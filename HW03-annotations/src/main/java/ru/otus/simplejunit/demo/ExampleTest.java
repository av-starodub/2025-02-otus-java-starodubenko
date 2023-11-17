<<<<<<< HEAD
package ru.otus.test.testclasses;
=======
package ru.otus.simplejunit.demo;
>>>>>>> e26d1b3 (Resolve merge conflict by incorporating both suggestions)

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import static ru.otus.simplejunit.util.Assertions.assertTrue;

public class BeforeMethodWithExceptionTest {
    @Before
    public void setUp() {
        throw new RuntimeException("Before method exception");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void checkSkippedTestMethod() {
        assertTrue(true);
    }
}
