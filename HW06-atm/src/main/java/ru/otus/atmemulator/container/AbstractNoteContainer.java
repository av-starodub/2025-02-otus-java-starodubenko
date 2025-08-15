package ru.otus.atmemulator.container;

import java.util.*;
import ru.otus.atmemulator.nominal.NominalType;

public abstract class AbstractNoteContainer implements NoteContainer {

    protected final Map<NominalType, Deque<NominalType>> banknotes;

    protected AbstractNoteContainer(Map<NominalType, Deque<NominalType>> banknotes) {
        Objects.requireNonNull(banknotes, " parameter banknotes must not be null");
        this.banknotes = new EnumMap<>(NominalType.class);
        this.banknotes.putAll(banknotes);
    }

    @Override
    public Map<NominalType, Integer> getNumberOfNotes() {
        Map<NominalType, Integer> numberOfNotes = new EnumMap<>(NominalType.class);
        banknotes.forEach((nominalType, notes) -> numberOfNotes.put(nominalType, notes.size()));
        return numberOfNotes;
    }

    protected static int computeAmount(Map<NominalType, Deque<NominalType>> banknotes) {
        final int[] amount = {0};
        banknotes.forEach((nominal, notes) -> amount[0] += nominal.getValue() * notes.size());
        return amount[0];
    }
}
