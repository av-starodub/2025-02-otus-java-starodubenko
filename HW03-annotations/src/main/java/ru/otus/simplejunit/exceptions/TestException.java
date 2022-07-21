package ru.otus.simplejunit.exceptions;

public class TestException extends RuntimeException {

    public TestException(String message, Exception e) {
        super(message, e);
    }

    public TestException(String message) {
        this(message, null);
    }

    public TestException(Exception e) {
        this(e.getMessage(), e);
    }
}
