package ru.otus.aop.proxy.util;


import ru.otus.aop.calculator.Calculator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {
    public static Calculator createLoggedCalculator(InvocationHandler handler) {
        return (Calculator) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{Calculator.class}, handler);
    }
}
