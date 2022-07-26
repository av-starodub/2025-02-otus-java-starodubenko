package ru.otus.simplejunit.cash;

import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;
import ru.otus.simplejunit.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

public class MethodsContainer {

    private final List<Method> before;
    private final List<Method> after;
    private final Set<Method> test;


    public MethodsContainer(Class<?> testClass) {
        before = new ArrayList<>();
        after = new ArrayList<>();
        test = new HashSet<>();
        saveMethodsInCash(testClass);
    }

    public List<Method> getBefore() {
        return before;
    }

    public List<Method> getAfter() {
        return after;
    }

    public Set<Method> getTest() {
        return test;
    }

    public void saveMethodsInCash(Class<?> testClass) {
        Arrays.stream(testClass.getMethods()).forEach(this::addIfAnnotationPresent);
    }

    private void addIfAnnotationPresent(Method method) {
        if (method.isAnnotationPresent(Before.class)) {
            before.add(method);
        }
        if (method.isAnnotationPresent(After.class)) {
            after.add(method);
        }
        if (method.isAnnotationPresent(Test.class)) {
            test.add(method);
        }
    }
}
