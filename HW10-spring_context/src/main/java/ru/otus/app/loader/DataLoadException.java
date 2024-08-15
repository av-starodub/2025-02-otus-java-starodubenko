package ru.otus.app.loader;

public class DataLoadException extends RuntimeException {
    public DataLoadException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
