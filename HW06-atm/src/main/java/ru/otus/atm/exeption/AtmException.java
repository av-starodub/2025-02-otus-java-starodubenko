package ru.otus.atm.exeption;

public class AtmException extends RuntimeException {

    public AtmException(String message, Exception e) {
        super(message, e);
    }

    public AtmException(String message) {
        this(message, null);
    }

    public AtmException(Exception e) {
        this(e.getMessage(), e);
    }
}
