package ru.otus.atm.container;

import ru.otus.atm.nominal.NominalType;

import java.util.*;

public abstract class AbstractNoteContainer implements NoteContainer {
    protected final Map<NominalType, Integer> banknotes;
    private final int amount;

    protected AbstractNoteContainer(Map<NominalType, Integer> banknotes) {
        Objects.requireNonNull(banknotes, " parameter banknotes must not be null");
        this.banknotes = new TreeMap<>(Comparator.comparingInt(NominalType::getValue));
        this.banknotes.putAll(banknotes);
        amount = computeAmount(banknotes);
    }

    @Override
    public int getBalance() {
        return amount;
    }

    @Override
    public Map<NominalType, Integer> getInfo() {
        return Map.copyOf(banknotes);
    }

    private static int computeAmount(Map<NominalType, Integer> banknotes) {
        final int[] amount = {0};
        banknotes.forEach((nominal, numberOfBanknotes) -> amount[0] += nominal.getValue() * numberOfBanknotes);
        return amount[0];
    }
}
