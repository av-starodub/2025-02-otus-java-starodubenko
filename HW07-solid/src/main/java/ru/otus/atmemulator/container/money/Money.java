package ru.otus.atmemulator.container.money;

import ru.otus.atmemulator.container.AbstractNoteContainer;
import ru.otus.atmemulator.container.builder.AbstractNoteBuilder;
import ru.otus.atmemulator.nominal.NominalType;

import java.util.Deque;
import java.util.Map;

public class Money extends AbstractNoteContainer {
    private final int amount;

    private Money(Map<NominalType, Deque<NominalType>> banknotes) {
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
        protected Money getInstance(Map<NominalType, Deque<NominalType>> banknotes) {
            return new Money(banknotes);
        }
    }
}
