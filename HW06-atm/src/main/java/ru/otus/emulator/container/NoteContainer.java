package ru.otus.emulator.container;

import ru.otus.emulator.nominal.NominalType;

import java.util.Map;

public interface NoteContainer {

    int getAmount();

    /**
     * @return must return unmodifiable map
     */
    Map<NominalType, Integer> getNumberOfNotes();
}
