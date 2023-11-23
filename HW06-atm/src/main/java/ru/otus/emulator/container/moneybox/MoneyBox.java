package ru.otus.emulator.container.moneybox;

import ru.otus.emulator.container.AbstractNoteContainer;
import ru.otus.emulator.container.NoteBox;
import ru.otus.emulator.container.NoteContainer;
import ru.otus.emulator.container.builder.AbstractNoteBuilder;
import ru.otus.emulator.container.money.Money;
import ru.otus.emulator.nominal.NominalType;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.stream.IntStream;

import static ru.otus.emulator.nominal.NominalType.*;

public class MoneyBox extends AbstractNoteContainer implements NoteBox {
    private final int ceilSize;
    private int amount;

    private MoneyBox(Map<NominalType, ArrayDeque<NominalType>> banknotes, int ceilSize) {
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
        protected MoneyBox getInstance(Map<NominalType, ArrayDeque<NominalType>> banknotes) {
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
                .put5000(extract(_5000, getRequired(request, _5000)))
                .put1000(extract(_1000, getRequired(request, _1000)))
                .put500(extract(_500, getRequired(request, _500)))
                .put100(extract(_100, getRequired(request, _100)))
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

    private ArrayDeque<NominalType> addStack(ArrayDeque<NominalType> stackIn, ArrayDeque<NominalType> stackToAdd) {
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
