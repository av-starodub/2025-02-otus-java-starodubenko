package ru.otus.aop.proxy.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.aop.proxy.annotation.Log;
import ru.otus.aop.proxy.reflection.AnnotatedMethodsExtractor;

/**
 * The class implements runtime logging of parameters for methods annotated with the @Log annotation.
 *
 * @param <T> any instance whose class implements any interface.
 */
public class LogMethodParametersHandler<T> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogMethodParametersHandler.class);

    private final T instance;

    private final Set<String> logAnnotatedMethods;

    public LogMethodParametersHandler(T instance) {
        this.instance = instance;
        logAnnotatedMethods = AnnotatedMethodsExtractor.extractAnnotatedMethods(instance.getClass(), Log.class);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var methodSignature = AnnotatedMethodsExtractor.getMethodSignature(method);
        if (logAnnotatedMethods.contains(methodSignature) && logger.isInfoEnabled()) {
            logger.info("executed method: {}, {}", method.getName(), logMethodParameters(args));
        }
        return method.invoke(instance, args);
    }

    private String logMethodParameters(Object[] args) {
        if (args == null || args.length == 0) {
            return "no-args";
        }
        var paramsLog = new StringBuilder();
        for (int idx = 0; idx < args.length; idx++) {
            if (idx > 0) {
                paramsLog.append(" ");
            }
            paramsLog.append("param").append(idx).append('=').append(args[idx]);
        }
        return paramsLog.toString();
    }
}
