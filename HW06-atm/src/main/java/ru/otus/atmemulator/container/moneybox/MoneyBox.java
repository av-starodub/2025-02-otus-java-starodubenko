package ru.otus.atmemulator.container.moneybox;

import static ru.otus.atmemulator.nominal.NominalType.*;

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
        return Money.builder()
                .put5000(extract(RUB_5000, getRequired(request, RUB_5000)))
                .put1000(extract(RUB_1000, getRequired(request, RUB_1000)))
                .put500(extract(RUB_500, getRequired(request, RUB_500)))
                .put100(extract(RUB_100, getRequired(request, RUB_100)))
                .build();
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

    private int getRequired(Map<NominalType, Integer> request, NominalType nominalType) {
        return request.getOrDefault(nominalType, 0);
    }
}
