package ru.otus.emulator.container.builder;

import ru.otus.emulator.container.NoteContainer;

public interface NoteBuilder<T extends NoteContainer> {
    T build();
}
