package ru.otus.aop.proxy.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class AnnotatedMethodsExtractor {

    private static final ConcurrentHashMap<Key, Set<String>> CACHE = new ConcurrentHashMap<>();

    private AnnotatedMethodsExtractor() {}

    private record Key(Class<?> clazz, Class<? extends Annotation> annotation) {

        Key {
            Objects.requireNonNull(clazz);
            Objects.requireNonNull(annotation);
        }
    }

    public static Set<String> extractAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        return CACHE.computeIfAbsent(
                new Key(clazz, annotation), key -> Arrays.stream(key.clazz().getDeclaredMethods())
                        .filter(m -> m.isAnnotationPresent(key.annotation()))
                        .map(AnnotatedMethodsExtractor::getMethodSignature)
                        .collect(Collectors.toUnmodifiableSet()));
    }

    public static String getMethodSignature(Method method) {
        var paramTypes = Arrays.stream(method.getParameterTypes())
                .map(type -> type.getCanonicalName() != null ? type.getCanonicalName() : type.getName())
                .collect(Collectors.joining(", "));
        return "%s:%d:%s".formatted(method.getName(), method.getParameterCount(), paramTypes);
    }
}
