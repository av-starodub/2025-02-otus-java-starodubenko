package ru.otus.atmemulator.container;

import ru.otus.atmemulator.nominal.NominalType;

import java.util.*;

public abstract class AbstractNoteContainer implements NoteContainer {
    protected final Map<NominalType, Integer> banknotes;

    protected AbstractNoteContainer(Map<NominalType, Integer> banknotes) {
        Objects.requireNonNull(banknotes, " parameter banknotes must not be null");
        this.banknotes = new EnumMap<>(NominalType.class);
        this.banknotes.putAll(banknotes);
    }

    @Override
    public Map<NominalType, Integer> getNumberOfNotes() {
        return Map.copyOf(banknotes);
    }

    protected static int computeAmount(Map<NominalType, Integer> banknotes) {
        final int[] amount = {0};
        banknotes.forEach((nominal, numberOfBanknotes) -> amount[0] += nominal.getValue() * numberOfBanknotes);
        return amount[0];
    }
}
