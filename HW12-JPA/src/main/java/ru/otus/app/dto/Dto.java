package ru.otus.app.dto;

public interface Dto<T> {
    T toDomainObject();
}
