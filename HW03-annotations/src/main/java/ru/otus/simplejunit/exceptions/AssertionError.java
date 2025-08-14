package ru.otus.simplejunit.exceptions;

public class AssertionError extends RuntimeException {
    public AssertionError(String message, Exception e) {
        super(message, e);
    }

    public AssertionError(String message) {
        this(message, null);
    }
}
