package ru.otus.test;

import ru.otus.simplejunit.runner.TestRunner;
import ru.otus.test.testclasses.*;

public class MainSimpleJunitTest {
    public static void main(String[] args) {
        new MainSimpleJunitTest().run(
                AfterMethodWithExceptionTest.class,
                "TestRunner should stop running after exception."
        );
        new MainSimpleJunitTest().run(
                BeforeMethodWithExceptionTest.class,
                "Test method should be skipped after exception."
        );
        new MainSimpleJunitTest().run(SeveralPreparatoryMethodsExecutionTest.class,
                "All preparatory methods should be completed."
        );
        new MainSimpleJunitTest().run(ExpectedExceptionInTestMethodTest.class,
                "Test method should be passed."
        );
        new MainSimpleJunitTest().run(UnexpectedExceptionInTestMethodTest.class,
                "Test method should be failed and TestRunner should continue running."
        );
        new MainSimpleJunitTest().run(TestMethodsExecutionTest.class,
                "Failed test method should throw TestException.");
    }

    private void run(Class<?> testClass, String expectations) {
        System.out.println("\n> " + testClass.getSimpleName());
        System.out.println(expectations);
        new TestRunner(testClass).run();
        System.out.println("---------------------------------");
    }
}
