package ru.otus.simplejunit.exceptions;

public class TestException extends RuntimeException {

    public TestException(String message, Throwable e) {
        super(message, e);
    }

    public TestException(String message) {
        this(message, null);
    }

    public TestException(Throwable e) {
        this(e.getMessage(), e);
    }
}
