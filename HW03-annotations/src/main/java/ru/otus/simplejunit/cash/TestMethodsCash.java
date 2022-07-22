package ru.otus.simplejunit.cash;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Class implements cash for all methods of test class annotated @Before, @After, @Test.
 * HashSet for @TEST annotated methods implements undefined test execution order.
 */
public enum TestMethodsCash {
    BEFORE(Before.class, new ArrayList<>()),
    AFTER(After.class, new ArrayList<>()),
    TEST(Test.class, new HashSet<>());

    private final Collection<Method> methods;
    private final Class<? extends Annotation> annotation;

    TestMethodsCash(Class<? extends Annotation> annotation, Collection<Method> methods) {
        this.methods = methods;
        this.annotation = annotation;
    }

    public Collection<Method> getMethods() {
        return methods;
    }

    public static void clearCash() {
        Arrays.stream(values()).forEach(methodType -> methodType.methods.clear());
    }

    public static void saveMethodsInCash(Class<?> testClass) {
        Arrays.stream(testClass.getMethods()).forEach(TestMethodsCash::addIfAnnotationPresent);
    }

    private static void addIfAnnotationPresent(Method method) {
        Arrays.stream(values()).forEach(cashType -> {
            if (method.isAnnotationPresent(cashType.annotation)) {
                cashType.methods.add(method);
            }
        });
    }
}
