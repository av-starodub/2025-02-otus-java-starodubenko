package ru.otus.test.testclasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ru.otus.simplejunit.annotations.After;
import ru.otus.simplejunit.annotations.Before;

public class SeveralPreparatoryMethodsExecutionTest {
    private List<String> expectedPreparatoryMethods;
    private List<String> actualPreparatoryMethods;

    public SeveralPreparatoryMethodsExecutionTest() {
        expectedPreparatoryMethods = new ArrayList<>() {
            {
                add("setUp1");
                add("setUp2");
                add("tearDown1");
                add("tearDown2");
            }
        };
        actualPreparatoryMethods = new ArrayList<>();
    }

    @Before
    public void setUp1() {
        actualPreparatoryMethods.add("setUp1");
    }

    @Before
    public void setUp2() {
        actualPreparatoryMethods.add("setUp2");
    }

    @After
    public void tearDown1() {
        actualPreparatoryMethods.add("tearDown1");
    }

    @After
    public void tearDown2() {
        actualPreparatoryMethods.add("tearDown2");
        printResult();
    }

    private boolean isAllExecuted() {
        return Objects.deepEquals(expectedPreparatoryMethods, actualPreparatoryMethods);
    }

    private void printResult() {
        System.out.println("All preparatory methods have been completed: " + isAllExecuted());
    }
}
