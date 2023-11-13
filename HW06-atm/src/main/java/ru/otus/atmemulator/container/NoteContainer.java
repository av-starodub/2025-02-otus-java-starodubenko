package ru.otus.atmemulator.container;

import ru.otus.atmemulator.nominal.NominalType;

import java.util.Map;

public interface NoteContainer {

    int getBalance();

    /**
     * @return must return unmodifiable map
     */
    Map<NominalType, Integer> getNumberOfNotes();
}
