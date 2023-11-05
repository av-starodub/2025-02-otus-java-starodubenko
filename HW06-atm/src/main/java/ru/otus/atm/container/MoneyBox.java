package ru.otus.atm.container;

import ru.otus.atm.nominal.NominalType;
import ru.otus.atm.builder.AbstractNoteBuilder;

import java.util.Map;

public class MoneyBox extends AbstractNoteContainer implements NoteBox {
    private final int ceilSize;
    private int amount;

    private MoneyBox(Map<NominalType, Integer> banknotes, int ceilSize) {
        super(banknotes);
        this.ceilSize = ceilSize;
        amount = getBalance();
    }

    public static MoneyBoxBuilder builder(int ceilSize) {
        return new MoneyBoxBuilder(ceilSize);
    }

    public static class MoneyBoxBuilder extends AbstractNoteBuilder<MoneyBox> {
        private final int ceilSize;

        public MoneyBoxBuilder(int ceilSize) {
            this.ceilSize = ceilSize;
        }

        @Override
        protected MoneyBox instanceOf(Map<NominalType, Integer> banknotes) {
            return new MoneyBox(banknotes, ceilSize);
        }
    }

    @Override
    public int putNotes(Map<NominalType, Integer> banknotes) {
        banknotes.forEach((nominal, numberOfBanknotes) -> {
            this.banknotes.merge(nominal, numberOfBanknotes, (key, oldValue) -> key + numberOfBanknotes);
            amount += nominal.getValue() * numberOfBanknotes;
        });
        return amount;
    }

    @Override
    public int extractNotes(Map<NominalType, Integer> banknotes) {
        banknotes.forEach((nominal, numberOfBanknotes) -> {
            this.banknotes.merge(nominal, numberOfBanknotes, (key, oldValue) -> key - numberOfBanknotes);
            amount -= nominal.getValue() * numberOfBanknotes;
        });
        return amount;
    }

    @Override
    public int getCeilSize() {
        return ceilSize;
    }
}
