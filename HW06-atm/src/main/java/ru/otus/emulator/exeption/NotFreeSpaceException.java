package ru.otus.emulator.exeption;

public class NotFreeSpaceException extends AtmException {
    public NotFreeSpaceException(String message) {
        super(message);
    }
}
