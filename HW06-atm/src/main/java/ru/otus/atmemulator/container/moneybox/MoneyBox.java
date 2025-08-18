package ru.otus.atmemulator.container.moneybox;

import java.util.Deque;
import java.util.Map;
import java.util.stream.IntStream;
import ru.otus.atmemulator.container.AbstractNoteContainer;
import ru.otus.atmemulator.container.NoteBox;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.container.builder.AbstractNoteBuilder;
import ru.otus.atmemulator.container.money.Money;
import ru.otus.atmemulator.nominal.NominalType;

public class MoneyBox extends AbstractNoteContainer implements NoteBox {

    private final int ceilSize;

    private int amount;

    private MoneyBox(Map<NominalType, Deque<NominalType>> banknotes, int ceilSize) {
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
        protected MoneyBox getInstance(Map<NominalType, Deque<NominalType>> banknotes) {
            return new MoneyBox(banknotes, ceilSize);
        }
    }

    @Override
    public int putNotes(NoteContainer money) {
        var notesToAdd = money.getNumberOfNotes();
        notesToAdd.forEach((nominal, number) -> {
            var stackToAdd = AbstractNoteBuilder.collectNotes(nominal, number);
            this.banknotes.merge(nominal, stackToAdd, this::addStack);
            amount += nominal.getValue() * number;
        });
        return amount;
    }

    @Override
    public NoteContainer extractNotes(Map<NominalType, Integer> request) {
        var builder = Money.builder();
        request.forEach((nominal, required) -> {
            var toExtract = required == null ? 0 : required;
            if (toExtract > 0) {
                var extracted = extract(nominal, toExtract);
                builder.put(nominal, extracted);
            }
        });
        return builder.build();
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getCeilSize() {
        return ceilSize;
    }

    private Deque<NominalType> addStack(Deque<NominalType> stackIn, Deque<NominalType> stackToAdd) {
        stackIn.addAll(stackToAdd);
        return stackIn;
    }

    private int extract(NominalType nominal, int numberOfNotes) {
        var stackIn = banknotes.get(nominal);
        IntStream.rangeClosed(1, numberOfNotes).forEach(extraction -> {
            stackIn.pollLast();
            amount -= nominal.getValue();
        });
        return numberOfNotes;
    }
}
