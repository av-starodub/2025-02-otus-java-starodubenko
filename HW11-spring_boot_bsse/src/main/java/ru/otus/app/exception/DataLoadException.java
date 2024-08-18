package ru.otus.app.exception;

public class DataLoadException extends RuntimeException {
    public DataLoadException(String msg, Throwable ex) {
        super(msg, ex);
    }

    public DataLoadException(String msg) {
        this(msg, null);
    }
}
