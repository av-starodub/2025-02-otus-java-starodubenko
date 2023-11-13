package ru.otus.atmemulator.container;

import ru.otus.atmemulator.nominal.NominalType;
import ru.otus.atmemulator.builder.AbstractNoteBuilder;

import java.util.Map;

public class Money extends AbstractNoteContainer {
    private final int amount;

    private Money(Map<NominalType, Integer> banknotes) {
        super(banknotes);
        amount = computeAmount(this.banknotes);
    }

    public static MoneyBuilder builder() {
        return new MoneyBuilder();
    }

    @Override
    public int getBalance() {
        return amount;
    }

    public static class MoneyBuilder extends AbstractNoteBuilder<Money> {
        @Override
        protected Money instanceOf(Map<NominalType, Integer> banknotes) {
            return new Money(banknotes);
        }
    }
}
