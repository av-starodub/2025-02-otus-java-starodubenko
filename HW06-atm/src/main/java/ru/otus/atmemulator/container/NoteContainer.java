package ru.otus.atmemulator.container;

import ru.otus.atmemulator.nominal.NominalType;

import java.util.Map;

public interface NoteContainer {

    int getAmount();

    /**
     * @return must return unmodifiable map
     */
    Map<NominalType, Integer> getNumberOfNotes();
}
