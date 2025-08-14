package ru.otus.orm.exceptions;

public class EntityMetaDataException extends RuntimeException {
    public EntityMetaDataException(String message, Throwable exception) {
        super(message, exception);
    }
}
