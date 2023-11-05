package ru.otus.atm.builder;

import ru.otus.atm.container.NoteContainer;

public interface NoteBuilder<T extends NoteContainer> {
    T build();
}
