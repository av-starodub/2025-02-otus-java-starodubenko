package ru.otus.atmemulator.container.money;

import java.util.Deque;
import java.util.Map;
import ru.otus.atmemulator.container.AbstractNoteContainer;
import ru.otus.atmemulator.container.builder.AbstractNoteBuilder;
import ru.otus.atmemulator.denomination.Note;

public class Money extends AbstractNoteContainer {

    private final int amount;

    private Money(Map<Note, Deque<Note>> banknotes) {
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
        protected Money getInstance(Map<Note, Deque<Note>> banknotes) {
            return new Money(banknotes);
        }
    }

    @Override
    public String toString() {
        return "Money{" + "amount=" + amount + ", banknotes=" + banknotes + '}';
    }
}
