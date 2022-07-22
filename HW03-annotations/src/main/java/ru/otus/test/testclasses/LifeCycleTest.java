package ru.otus.test.testclasses;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

/**
 * The class checks that each method annotated with @Test is executed on a new instance of the test class.
 */
public class LifeCycleTest {
    @Before
    public void setUp() {
        printCurrentInstanceHashCode("\n@Before");
    }

    @After
    public void tearDown() {
        printCurrentInstanceHashCode("@After");
    }

    @Test
    public void firstTest() {
        printCurrentInstanceHashCode("@Test: firstTest");
    }

    @Test
    public void secondTest() {
        printCurrentInstanceHashCode("@Test: secondTest");
    }

    private void printCurrentInstanceHashCode(String methodType) {
        System.out.printf("%s. Test class instance: %s\n", methodType, Integer.toHexString(hashCode()));
    }
}
