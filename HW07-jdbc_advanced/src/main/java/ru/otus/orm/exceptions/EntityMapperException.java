package ru.otus.orm.exceptions;

public class EntityMapperException extends RuntimeException {

    public EntityMapperException(String message, Throwable exception) {
        super(message, exception);
    }
}
