package ru.otus.aop.proxy.handlers;

import static ru.otus.aop.proxy.util.AnnotatedMethodsSupplier.createKey;
import static ru.otus.aop.proxy.util.AnnotatedMethodsSupplier.getAnnotatedMethods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Set;
import ru.otus.aop.proxy.annotations.Log;

/**
 * The class implements the output to the console of the parameters
 * of the class methods marked with the annotation @Log at runtime.
 *
 * @param <T> any instance whose class implements any interface.
 */
public class LogMethodParametersHandler<T> implements InvocationHandler {
    private final T instance;
    private final Set<String> logAnnotatedMethods;

    public LogMethodParametersHandler(T instance) {
        this.instance = instance;
        logAnnotatedMethods = getAnnotatedMethods(instance.getClass(), Log.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (logAnnotatedMethods.contains(createKey(method))) {
            printLog(createMethodParametersLog(method, args));
        }
        return method.invoke(instance, args);
    }

    private String createMethodParametersLog(Method method, Object[] args) {
        StringBuilder log = new StringBuilder();
        log.append(String.format("executed method: %s, ", method.getName()));

        if (Objects.nonNull(args)) {
            for (int idx = 0; idx < args.length; idx++) {
                log.append(String.format("param%d=%s ", idx, args[idx]));
            }
        }
        return log.toString();
    }

    private void printLog(String log) {
        System.out.println(log);
    }
}
