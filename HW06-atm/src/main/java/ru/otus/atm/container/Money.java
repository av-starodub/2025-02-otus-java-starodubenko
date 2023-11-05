package ru.otus.atm.container;

import ru.otus.atm.nominal.NominalType;
import ru.otus.atm.builder.AbstractNoteBuilder;

import java.util.Map;

public class Money extends AbstractNoteContainer {

    private Money(Map<NominalType, Integer> banknotes) {
        super(banknotes);
    }

    public static MoneyBuilder builder() {
        return new MoneyBuilder();
    }

    public static class MoneyBuilder extends AbstractNoteBuilder<Money> {
        @Override
        protected Money instanceOf(Map<NominalType, Integer> banknotes) {
            return new Money(banknotes);
        }
    }
}
