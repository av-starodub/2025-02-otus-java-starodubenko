package ru.otus.simplejunit.util;

import ru.otus.simplejunit.cash.MethodsContainer;
import ru.otus.simplejunit.cash.ResultContainer;
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
        ResultContainer results = new ResultContainer();
        try {
            runTests(testClass, results);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            printResults(results);
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

    private static void runTests(Class<?> testClass, ResultContainer results) {
        MethodsContainer cash = new MethodsContainer(testClass);
        for (Method test : cash.getTest()) {
            Object testCase = new Object();
            try {
                testCase = createTestCase(testClass);
                try {
                    runForEachTest(testCase, cash.getBefore());
                } catch (Exception e) {
                    results.addSkipped();
                    e.printStackTrace();
                    break;
                }
                test.invoke(testCase);
                results.addPassed();
            } catch (Exception e) {
                results.addFailed();
                e.printStackTrace();
            } finally {
                runForEachTest(testCase, cash.getAfter());
            }
        }
    }

    private static void printResults(ResultContainer results) {
        System.out.printf(results.toString());
    }
}
