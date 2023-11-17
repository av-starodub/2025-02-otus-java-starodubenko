package ru.otus.atmemulator.container.money;

import ru.otus.atmemulator.container.AbstractNoteContainer;
import ru.otus.atmemulator.nominal.NominalType;
import ru.otus.atmemulator.container.builder.AbstractNoteBuilder;

import java.util.ArrayDeque;
import java.util.Map;

public class Money extends AbstractNoteContainer {
    private final int amount;

    private Money(Map<NominalType, ArrayDeque<NominalType>> banknotes) {
        super(banknotes);
        amount = computeAmount(this.banknotes);
    }

    public static MoneyBuilder builder() {
        return new MoneyBuilder();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    public static class MoneyBuilder extends AbstractNoteBuilder<Money> {
        @Override
        protected Money getInstance(Map<NominalType, ArrayDeque<NominalType>> banknotes) {
            return new Money(banknotes);
        }
    }
}
