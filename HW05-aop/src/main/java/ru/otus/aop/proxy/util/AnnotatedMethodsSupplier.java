package ru.otus.aop.proxy.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class AnnotatedMethodsSupplier {
    private AnnotatedMethodsSupplier() {}

    public static Set<String> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        var annotatedMethods = new HashSet<String>();
        Arrays.stream(clazz.getDeclaredMethods()).forEach(method -> {
            if (method.isAnnotationPresent(annotation)) {
                annotatedMethods.add(createKey(method));
            }
        });
        return annotatedMethods;
    }

    public static String createKey(Method method) {
        return method.getName() + Arrays.toString(method.getParameters());
    }
}
