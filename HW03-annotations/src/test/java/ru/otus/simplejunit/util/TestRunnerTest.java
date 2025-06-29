package ru.otus.simplejunit.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.stubbing.Answer;
import ru.otus.simplejunit.cash.MethodsContainer;
import ru.otus.simplejunit.cash.ResultsContainer;
import ru.otus.simplejunit.scenarios.*;
import ru.otus.simplejunit.scenarios.util.CallMethodWriter;

@DisplayName("TestRunnerTest")
public class TestRunnerTest {
    private Class<?> testClassName;
    private MethodsContainer mc;
    private ResultsContainer rc;
    private final Method runTests;
    private final List<String> methodCalls;
    private final MockedStatic<CallMethodWriter> writer;
    private final Answer<Void> addMethod;

    public TestRunnerTest() {
        runTests = getRunTestsMethod();
        methodCalls = spy(new ArrayList<>());
        writer = mockStatic(CallMethodWriter.class);
        addMethod = invocation -> {
            String name = invocation.getArgument(0);
            methodCalls.add(name);
            return null;
        };
    }

    @AfterEach
    public void tearDown() {
        testClassName = null;
        mc = null;
        rc = null;
        methodCalls.clear();
        writer.close();
    }

    @Test
    @DisplayName("lifeCycleTest: TestRunner must create a new test class instance for each test method.")
    public void lifeCycleTest() {
        createTestEnvironment(LifeCycleTest.class);
        var testClass = (LifeCycleTest) spy(testClassName);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        assertEquals(2, testClass.getTestsHashCodes().size());
    }

    @Test
    @DisplayName("TestMethodsExecutionTest: failed test should throw TestException and TestRunner must not stop")
    public void TestMethodsExecutionTest() {
        createTestEnvironment(TestMethodsExecutionTest.class);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        assertEquals(1, rc.getErrors().size());
        assertTrue(rc.getErrors().get(0).getLocalizedMessage().endsWith("FAILED"));
        assertEquals("TESTS - 2, PASSED - 1, FAILED - 1", rc.getStatistic());
    }

    @Test
    @DisplayName("SeveralPreparatoryMethodsExecutionTest: all setUp() and all tearDown() methods should be completed.")
    public void SeveralPreparatoryMethodsExecutionTest() {
        createTestEnvironment(SeveralPreparatoryMethodsExecutionTest.class);
        writer.when(() -> CallMethodWriter.call(anyString())).thenAnswer(addMethod);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        String[] expectedOrder = {"setUp1", "setUp2", "anyTest", "tearDown1", "tearDown2"};
        assertArrayEquals(expectedOrder, methodCalls.toArray());
    }

    @Test
    @DisplayName("AfterMethodWithExceptionTest: test should be called, result should fail, TestRunner should not stop.")
    public void AfterMethodWithExceptionTest() {
        createTestEnvironment(AfterMethodWithExceptionTest.class);
        writer.when(() -> CallMethodWriter.call(anyString())).thenAnswer(addMethod);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        // checking that tests have been called
        verify(methodCalls, times(1)).add("firstTest");
        verify(methodCalls, times(1)).add("secondTest");
        // checking the results of TestRunner
        assertEquals(2, rc.getErrors().size());
        assertTrue(rc.getErrors().get(0).getLocalizedMessage().endsWith("tearDown"));
        assertTrue(rc.getErrors().get(1).getLocalizedMessage().endsWith("tearDown"));
        assertEquals("TESTS - 2, PASSED - 0, FAILED - 2", rc.getStatistic());
    }

    @Test
    @DisplayName(
            "BeforeMethodWithExceptionTest: test should not be called, result should fail, TestRunner should not stop.")
    public void BeforeMethodWithExceptionTest() {
        createTestEnvironment(BeforeMethodWithExceptionTest.class);
        writer.when(() -> CallMethodWriter.call(anyString())).thenAnswer(addMethod);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        // checking that tests have not been called
        verify(methodCalls, times(0)).add("firstTest");
        verify(methodCalls, times(0)).add("secondTest");
        // checking the results of TestRunner
        assertEquals(2, rc.getErrors().size());
        assertTrue(rc.getErrors().get(0).getLocalizedMessage().endsWith("setUp"));
        assertTrue(rc.getErrors().get(1).getLocalizedMessage().endsWith("setUp"));
        assertEquals("TESTS - 2, PASSED - 0, FAILED - 2", rc.getStatistic());
    }

    @Test
    @DisplayName("ExpectedExceptionInTestMethodTest: should not be throwing an exception.")
    public void ExpectedExceptionInTestMethodTest() {
        createTestEnvironment(ExpectedExceptionInTestMethodTest.class);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        assertEquals(0, rc.getErrors().size());
        assertEquals("TESTS - 1, PASSED - 1, FAILED - 0", rc.getStatistic());
    }

    @Test
    @DisplayName("UnexpectedExceptionInTestMethodTest: result should fail, TestRunner should not stop.")
    public void UnexpectedExceptionInTestMethodTest() {
        createTestEnvironment(UnexpectedExceptionInTestMethodTest.class);
        invokeRunTestsMethod(runTests, testClassName, mc, rc);
        assertEquals(1, rc.getErrors().size());
        assertTrue(rc.getErrors().get(0).getLocalizedMessage().endsWith("FAILED"));
        assertEquals("TESTS - 3, PASSED - 2, FAILED - 1", rc.getStatistic());
    }

    private void createTestEnvironment(Class<?> testClass) {
        this.testClassName = testClass;
        mc = spy(new MethodsContainer(testClass));
        rc = spy(new ResultsContainer(mc.getTest()));
    }

    private Method getRunTestsMethod() {
        try {
            return TestRunner.class.getDeclaredMethod(
                    "runTests", Class.class, MethodsContainer.class, ResultsContainer.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void invokeRunTestsMethod(Method runTests, Class<?> testClass, MethodsContainer mc, ResultsContainer rc) {
        try {
            runTests.setAccessible(true);
            runTests.invoke(null, testClass, mc, rc);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
