package ru.otus.simplejunit.scenarios;

import java.util.HashSet;
import java.util.Set;
import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

/**
 * The class implement checking that each method annotated with @Test is executed on a new instance of the test class.
 * As a result of running this script, the size of HASH_CODES should be equal to 2.
 */
public class LifeCycleTest {

    private static final Set<Integer> HASH_CODES = new HashSet<>();

    public Set<Integer> getTestsHashCodes() {
        return HASH_CODES;
    }

    @Before
    public void setUp() {
        HASH_CODES.add(hashCode());
    }

    @After
    public void tearDown() {
        HASH_CODES.add(hashCode());
    }

    @Test
    public void firstTest() {
        HASH_CODES.add(hashCode());
    }

    @Test
    public void secondTest() {
        HASH_CODES.add(hashCode());
    }
}
