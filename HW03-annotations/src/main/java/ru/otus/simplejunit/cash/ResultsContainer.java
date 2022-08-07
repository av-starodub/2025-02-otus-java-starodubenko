package ru.otus.simplejunit.cash;

import java.lang.reflect.Method;
import java.util.*;

public class ResultsContainer {
    private int passed;
    private int failed;
    private int uncalled;
    private final int tests;
    private final HashMap<String, Integer> results;
    private final List<Throwable> errors;

    public ResultsContainer(Set<Method> methods) {
        results = new HashMap<>() {{
            methods.forEach(method -> this.put(method.getName(), -1));
        }};
        tests = results.size();
        errors = new ArrayList<>();
    }

    public void pass(String methodName) {
        results.put(methodName, 1);
    }

    public void fail(String methodName) {
        results.put(methodName, 0);
    }

    public void addError(Throwable e) {
        errors.add(e);
    }

    private void computeStatistic() {
        for (var value : results.values()) {
            if (value == -1) {
                uncalled++;
            }
            passed += value == 1 ? 1 : 0;
        }
        failed = tests - passed - uncalled;
    }

    public HashMap<String, Integer> getResults() {
        return results;
    }

    public String getStatistic() {
        computeStatistic();
        return String.format("TESTS - %d, PASSED - %d, FAILED - %d", tests, passed, failed);
    }

    public List<Throwable> getErrors() {
        return errors;
    }

    public void printStatistic() {
        System.out.println(getStatistic());
    }

    public void printCausesOfFailures() {
        for (var error : errors) {
            error.printStackTrace();
        }
    }
}
