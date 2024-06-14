package ru.otus.advjdbc.exceptions;

public class AbstractRepositoryException extends RuntimeException {

    public AbstractRepositoryException(String message) {
        super(message);
    }

    public AbstractRepositoryException(String message, Throwable exception) {
        super(message, exception);
    }
}
