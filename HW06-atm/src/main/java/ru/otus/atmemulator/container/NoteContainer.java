package ru.otus.atmemulator.container;

import java.util.Map;
import ru.otus.atmemulator.nominal.NominalType;

public interface NoteContainer {

    int getAmount();

    /**
     * @return must return an unmodifiable map
     */
    Map<NominalType, Integer> getNumberOfNotes();
}
