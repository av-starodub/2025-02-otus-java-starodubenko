package ru.otus.emulator.container;

import ru.otus.emulator.nominal.NominalType;

import java.util.Map;

public interface NoteBox extends NoteContainer {
    int putNotes(NoteContainer money);

    NoteContainer extractNotes(Map<NominalType, Integer> banknotes);

    int getCeilSize();
}
