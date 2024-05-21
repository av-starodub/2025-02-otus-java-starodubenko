package ru.otus.advjdbc.executors;

public class DataBaseOperationException extends RuntimeException {

    public DataBaseOperationException(String message, Throwable exception) {
        super(message, exception);
    }
}
