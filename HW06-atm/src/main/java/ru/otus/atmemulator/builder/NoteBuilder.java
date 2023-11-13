package ru.otus.atmemulator.builder;

import ru.otus.atmemulator.container.NoteContainer;

public interface NoteBuilder<T extends NoteContainer> {
    T build();
}
