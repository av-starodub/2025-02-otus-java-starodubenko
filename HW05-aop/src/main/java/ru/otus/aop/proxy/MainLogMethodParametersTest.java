package ru.otus.aop.proxy;

import ru.otus.aop.calculator.Calculator;
import ru.otus.aop.calculator.operations.Addition;
import ru.otus.aop.proxy.handlers.LogMethodParametersHandler;
import ru.otus.aop.proxy.util.Ioc;

public class MainLogMethodParametersTest {
    private final Calculator sum;

    private MainLogMethodParametersTest() {
        sum = Ioc.createLoggedCalculator(new LogMethodParametersHandler<>(new Addition()));
    }

    public static void main(String[] args) {
        new MainLogMethodParametersTest().runTests();
    }

    private void runTests() {
        doTest("check method without parameters annotated with @Log",
                sum::calculate,
                "executed method: calculate,"
        );

        doTest("check empty log for method not annotated with @Log",
                () -> sum.calculate(1),
                ""
        );
        doTest(
                "check correct logging of parameters of the same type",
                () -> sum.calculate(1, 2),
                "executed method: calculate, param0=1 param1=2"
        );
        doTest(
                "check correct logging of parameters of different types",
                () -> sum.calculate(2, 3, "km/h"),
                "executed method: calculate, param0=2 param1=3 param2=km/h"
        );
    }

    private void doTest(String description, TestProcessor test, String expectedLog) {
        printTestRepresentation(description, expectedLog);
        test.process();
    }

    private void printTestRepresentation(String description, String expectedLog) {
        System.out.println("\n____________________________");
        System.out.println("> MainLoggingParametersTest: " + description);
        System.out.println("expected log: " + expectedLog);
        System.out.print("  actual log: ");
    }

    @FunctionalInterface
    private interface TestProcessor {
        void process();
    }
<<<<<<< HEAD
}
=======
public class MainLogMethodParametersTest {
}
>>>>>>> 76d2049 (Resolve merge conflict by incorporating both suggestions)
=======
}
>>>>>>> fc651c0 (Resolve merge conflict by incorporating both suggestions)
