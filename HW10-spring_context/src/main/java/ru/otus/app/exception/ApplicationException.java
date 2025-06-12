package ru.otus.app.exception;

public class ApplicationException extends RuntimeException {
    public ApplicationException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
