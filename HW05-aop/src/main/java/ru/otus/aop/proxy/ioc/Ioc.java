package ru.otus.aop.proxy.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import ru.otus.aop.calculator.Calculator;

public final class Ioc {

    private Ioc() {}

    public static Calculator createLoggedCalculator(InvocationHandler handler) {
        return (Calculator)
                Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[] {Calculator.class}, handler);
    }
}
