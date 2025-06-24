package ru.otus.atmemulator.container;

import ru.otus.atmemulator.nominal.NominalType;

import java.util.Map;

public interface NoteBox extends NoteContainer {
    int putNotes(NoteContainer money);

    NoteContainer extractNotes(Map<NominalType, Integer> banknotes);

    int getCeilSize();
}
