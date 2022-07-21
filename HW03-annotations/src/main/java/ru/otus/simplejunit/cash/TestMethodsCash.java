package ru.otus.simplejunit.cash;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

/**
 * Class implements cash for all methods of test class annotated @Before, @After, @Test.
 * HashSet for @TEST annotated methods implements undefined test execution order.
 */
public enum TestMethodsCash {
    BEFORE(new ArrayList<>()),
    AFTER(new ArrayList<>()),
    TEST(new HashSet<>());

    private final Collection<Method> methods;

    TestMethodsCash(Collection<Method> methods) {
        this.methods = methods;
    }

    public Collection<Method> getMethods() {
        return methods;
    }
}
