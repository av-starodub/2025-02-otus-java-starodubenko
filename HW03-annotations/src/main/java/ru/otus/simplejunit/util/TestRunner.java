package ru.otus.simplejunit.util;

import ru.otus.simplejunit.cash.*;
import ru.otus.simplejunit.exceptions.TestException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Objects;

public final class TestRunner {
    private TestRunner() {
    }

    public static void run(Class<?> testClass) {
        if (Objects.isNull(testClass)) {
            throw new TestException("Test class must not be null.");
        }
        var mc = new MethodsContainer(testClass);
        var rc = new ResultsContainer(mc.getTest());
        runTests(testClass, mc, rc);
        rc.printStatistic();
        rc.printCausesOfFailures();
    }

    private static Object createTestCase(Class<?> testClass) {
        try {
            return testClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new TestException("Error instantiating test class", e);
        }
    }

    private static void runForEachTest(Object testCase, Collection<Method> methods, String testMethodName, ResultsContainer rc) {
        for (var method : methods) {
            try {
                method.invoke(testCase);
            } catch (Throwable e) {
                rc.fail(testMethodName);
                throw new TestException("Test: " + testMethodName + " FAILED" + " - error in preparatory method: " + method.getName(), e);
            }
        }
    }

    private static void runTest(Object testCase, Method test, ResultsContainer rc) {
        try {
            test.invoke(testCase);
            rc.pass(test.getName());
        } catch (Throwable e) {
            rc.fail(test.getName());
            throw new TestException("Test: " + test.getName() + " - FAILED", e);
        }
    }

    private static void runTests(Class<?> testClass, MethodsContainer mc, ResultsContainer rc) {
        for (var test : mc.getTest()) {
            var testMethodName = test.getName();
            var testCase = new Object();
            try {
                try {
                    testCase = createTestCase(testClass);
                    runForEachTest(testCase, mc.getBefore(), testMethodName, rc);
                    runTest(testCase, test, rc);
                } catch (Throwable e) {
                    rc.addError(e);
                } finally {
                    runForEachTest(testCase, mc.getAfter(), testMethodName, rc);
                }
            } catch (Throwable e) {
                rc.addError(e);
            }
        }
    }
}
