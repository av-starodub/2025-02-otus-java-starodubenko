package ru.otus.atmemulator.container.moneybox;

import java.util.Deque;
import java.util.Map;
import java.util.stream.IntStream;
import ru.otus.atmemulator.container.AbstractNoteContainer;
import ru.otus.atmemulator.container.NoteBox;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.container.builder.AbstractNoteBuilder;
import ru.otus.atmemulator.container.money.Money;
import ru.otus.atmemulator.denomination.Note;

public class MoneyBox extends AbstractNoteContainer implements NoteBox {

    private final int ceilSize;

    private int amount;

    private final int minNominal;

    private MoneyBox(Map<Note, Deque<Note>> banknotes, int ceilSize) {
        super(banknotes);
        amount = computeAmount(this.banknotes);
        this.ceilSize = ceilSize;
        minNominal = banknotes.keySet().stream()
                .mapToInt(Note::getNominalValue)
                .min()
                .orElse(0);
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
        protected MoneyBox getInstance(Map<Note, Deque<Note>> banknotes) {
            return new MoneyBox(banknotes, ceilSize);
        }
    }

    @Override
    public int putNotes(NoteContainer money) {
        var notesToAdd = money.getNumberOfNotes();
        notesToAdd.forEach((note, number) -> {
            var stackToAdd = AbstractNoteBuilder.collectNotes(note, number);
            this.banknotes.merge(note, stackToAdd, this::addStack);
            amount += note.getNominalValue() * number;
        });
        return amount;
    }

    @Override
    public NoteContainer extractNotes(Map<Note, Integer> request) {
        var builder = Money.builder();
        request.forEach((note, required) -> {
            var toExtract = required == null ? 0 : required;
            if (toExtract > 0) {
                var extracted = extract(note, toExtract);
                builder.put(note, extracted);
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

    @Override
    public int getMinNominal() {
        return minNominal;
    }

    private Deque<Note> addStack(Deque<Note> stackIn, Deque<Note> stackToAdd) {
        stackIn.addAll(stackToAdd);
        return stackIn;
    }

    private int extract(Note note, int numberOfNotes) {
        var stackIn = banknotes.get(note);
        IntStream.rangeClosed(1, numberOfNotes).forEach(extraction -> {
            stackIn.pollLast();
            amount -= note.getNominalValue();
        });
        return numberOfNotes;
    }
}
