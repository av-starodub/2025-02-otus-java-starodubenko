package ru.otus.atm.container;

import ru.otus.atm.nominal.NominalType;

import java.util.Map;

public interface NoteBox extends NoteContainer {
    int putNotes(Map<NominalType, Integer> banknotes);

    int extractNotes(Map<NominalType, Integer> banknotes);

    int getCeilSize();
}
