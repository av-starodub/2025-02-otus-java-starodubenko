package ru.otus.test;

import ru.otus.simplejunit.util.TestRunner;
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
                "Test with unexpected exception should be failed and TestRunner must not stop executing tests.",
                "PASSED 2, FAILED 1, SKIPPED 0"
        );
        new MainSimpleJunitTest().run(TestMethodsExecutionTest.class,
                "Failed test should throw TestException and TestRunner must not stop executing tests.",
                "PASSED 1, FAILED 1, SKIPPED 0"
        );
        new MainSimpleJunitTest().run(LifeCycleTest.class,
                "Each method annotated with @Test must be executed on a new instance of the test class.",
                "The hashCode of the test class instance must be different for each test method."
        );
    }

    private void run(Class<?> testClass, String testDescription, String expectedResult) {
        System.out.println("\n> Test class: " + testClass.getSimpleName());
        System.out.println("Description: " + testDescription);
        TestRunner.run(testClass);
        System.out.println("Expected result: " + expectedResult);
        System.out.println("---------------------------------");
    }
}
