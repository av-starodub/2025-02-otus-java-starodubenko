package ru.otus.aop.proxy.util;

<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> fc651c0 (Resolve merge conflict by incorporating both suggestions)

import ru.otus.aop.calculator.Calculator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Ioc {
    public static Calculator createLoggedCalculator(InvocationHandler handler) {
        return (Calculator) Proxy.newProxyInstance(Ioc.class.getClassLoader(), new Class<?>[]{Calculator.class}, handler);
    }
<<<<<<< HEAD
=======
public class Ioc {
>>>>>>> 76d2049 (Resolve merge conflict by incorporating both suggestions)
=======
>>>>>>> fc651c0 (Resolve merge conflict by incorporating both suggestions)
}
