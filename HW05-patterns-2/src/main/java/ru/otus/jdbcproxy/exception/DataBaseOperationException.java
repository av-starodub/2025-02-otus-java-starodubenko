package ru.otus.jdbcproxy.exception;

public class DataBaseOperationException extends RuntimeException {

    public DataBaseOperationException(String message, Throwable exception) {
        super(message, exception);
    }
}
