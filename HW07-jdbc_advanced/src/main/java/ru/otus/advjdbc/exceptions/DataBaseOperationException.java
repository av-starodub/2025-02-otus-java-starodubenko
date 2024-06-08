package ru.otus.advjdbc.exceptions;

public class DataBaseOperationException extends RuntimeException {

    public DataBaseOperationException(String message, Throwable exception) {
        super(message, exception);
    }
}
