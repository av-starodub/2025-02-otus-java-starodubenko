package ru.otus.test;

import ru.otus.simplejunit.runner.TestRunner;
import ru.otus.test.testclasses.*;

public class MainSimpleJunitTest {
    public static void main(String[] args) {
        new MainSimpleJunitTest().run(
                AfterMethodWithExceptionTest.class,
                "TestRunner should stop executing tests after exception in method annotated with @After.",
                "PASSED 1, FAILED 0, SKIPPED 0"
        );
        new MainSimpleJunitTest().run(
                BeforeMethodWithExceptionTest.class,
                "Test should be skipped after exception in method annotated with @Before.",
                "PASSED 0, FAILED 0, SKIPPED 1"
        );
        new MainSimpleJunitTest().run(SeveralPreparatoryMethodsExecutionTest.class,
                "Two setUp() and two tearDown() methods should be completed.",
                "true"
        );
        new MainSimpleJunitTest().run(ExpectedExceptionInTestMethodTest.class,
                "Test with expected exception should be passed without throw exception.",
                "PASSED 1, FAILED 0, SKIPPED 0"
        );
        new MainSimpleJunitTest().run(UnexpectedExceptionInTestMethodTest.class,
                "Test with unexpected exception should be failed and TestRunner should continue running.",
                "PASSED 2, FAILED 1, SKIPPED 0"
        );
        new MainSimpleJunitTest().run(TestMethodsExecutionTest.class,
                "Failed test should throw TestException and TestRunner should continue running.",
                "PASSED 1, FAILED 1, SKIPPED 0"
        );
    }

    private void run(Class<?> testClass, String testDescription, String expectedResult) {
        System.out.println("\n> " + testClass.getSimpleName());
        System.out.println(testDescription);
        new TestRunner(testClass).run();
        System.out.println("Expected result: " + expectedResult);
        System.out.println("---------------------------------");
    }
}
