package ru.otus.atmemulator.container;

import java.util.Map;
import ru.otus.atmemulator.nominal.NominalType;

public interface NoteBox extends NoteContainer {

    int putNotes(NoteContainer money);

    NoteContainer extractNotes(Map<NominalType, Integer> banknotes);

    int getCeilSize();
}
