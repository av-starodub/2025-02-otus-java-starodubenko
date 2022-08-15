package ru.otus.aop.proxy.handlers;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogMethodParametersHandler<T> implements InvocationHandler {
    public LogMethodParametersHandler(T instance) {
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
