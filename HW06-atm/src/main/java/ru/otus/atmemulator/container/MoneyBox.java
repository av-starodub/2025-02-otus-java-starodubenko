package ru.otus.atmemulator.container;

import ru.otus.atmemulator.builder.AbstractNoteBuilder;
import ru.otus.atmemulator.nominal.NominalType;

import java.util.Map;

public class MoneyBox extends AbstractNoteContainer implements NoteBox {
    private final int ceilSize;
    private int amount;

    private MoneyBox(Map<NominalType, Integer> banknotes, int ceilSize) {
        super(banknotes);
        amount = computeAmount(this.banknotes);
        this.ceilSize = ceilSize;
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
            this.banknotes.merge(nominal, numberOfBanknotes, Integer::sum);
            amount += nominal.getValue() * numberOfBanknotes;
        });
        return amount;
    }

    @Override
    public NoteContainer extractNotes(Map<NominalType, Integer> banknotes) {
        banknotes.forEach((nominal, numberOfBanknotes) -> {
            this.banknotes.merge(nominal, numberOfBanknotes, (oldValue, newValue) -> oldValue - numberOfBanknotes);
            amount -= nominal.getValue() * numberOfBanknotes;
        });
        return Money.builder()
                .put5000(banknotes.getOrDefault(NominalType._5000, 0))
                .put1000(banknotes.getOrDefault(NominalType._1000, 0))
                .put500(banknotes.getOrDefault(NominalType._500, 0))
                .put100(banknotes.getOrDefault(NominalType._100, 0))
                .build();
    }

    @Override
    public int getBalance() {
        return amount;
    }

    @Override
    public int getCeilSize() {
        return ceilSize;
    }
}
