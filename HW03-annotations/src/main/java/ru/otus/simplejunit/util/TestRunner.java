package ru.otus.simplejunit.util;

import ru.otus.simplejunit.exceptions.TestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

import static ru.otus.simplejunit.cash.TestMethodsCash.*;
import static ru.otus.simplejunit.logger.TestResultLogger.*;

public final class TestRunner {
    private TestRunner() {
    }

    public static void run(Class<?> testClass) {
        if (Objects.isNull(testClass)) {
            throw new TestException("Test class must not be null.");
        }
        saveMethodsInCash(testClass);
        try {
            runTests(testClass);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            printResults();
            clearCash();
            resetLogger();
        }
    }

    private static Object createTestCase(Class<?> testClass) {
        try {
            return testClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new TestException("Error instantiating test class", e);
        }
    }

    private static void runForEachTest(Object testCase, Collection<Method> methods) {
        for (Method method : methods) {
            try {
                method.invoke(testCase);
            } catch (Exception e) {
                throw new TestException("Error in preparatory method: " + method.getName(), e);
            }
        }
    }

    private static void runTests(Class<?> testClass) {
        for (Method test : TEST.getMethods()) {
            Object testCase = new Object();
            try {
                testCase = createTestCase(testClass);
                try {
                    runForEachTest(testCase, BEFORE.getMethods());
                } catch (Exception e) {
                    SKIPPED.addEvent();
                    e.printStackTrace();
                    break;
                }
                test.invoke(testCase);
            } catch (Exception e) {
                FAILED.addEvent();
                e.printStackTrace();
            } finally {
                runForEachTest(testCase, AFTER.getMethods());
            }
        }
    }

    private static void printResults() {
        System.out.printf("TEST RESULTS: %s, %s, %s\n", PASSED, FAILED, SKIPPED);
    }
}
