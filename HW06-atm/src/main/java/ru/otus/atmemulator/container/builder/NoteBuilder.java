package ru.otus.atmemulator.container.builder;

import ru.otus.atmemulator.container.NoteContainer;

public interface NoteBuilder<T extends NoteContainer> {

    T build();
}
