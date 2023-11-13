package ru.otus.atmemulator.exeption;

public class NotFreeSpaceException extends AtmException {
    public NotFreeSpaceException(String message, Exception e) {
        super(message, e);
    }

    public NotFreeSpaceException(String message) {
        super(message);
    }

    public NotFreeSpaceException(Exception e) {
        super(e);
    }
}
