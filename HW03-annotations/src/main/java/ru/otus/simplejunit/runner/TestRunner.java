package ru.otus.simplejunit.runner;

import java.lang.reflect.Method;
import java.util.Collection;

public class TestRunner {
    public TestRunner(Class<?> testClass) {
        runClass(testClass);
    }

    public void runClass(Class<?> testClass) {
        getMethods(testClass);
        runTests(testClass);
        printResults();
    }

    private void getMethods(Class<?> testClass) {
    }

    private Object createTestCase(Class<?> testClass) {
        return null;
    }
    private void runForEachTest(Object testCase, Collection<Method> methods) {
    }

    private void runTests(Class<?> testClass) {
    }

    private void printResults() {
    }
}
