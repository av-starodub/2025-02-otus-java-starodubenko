package ru.otus.atmemulator.container.builder;

import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.nominal.NominalType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.IntStream;

import static ru.otus.atmemulator.nominal.NominalType.*;

public abstract class AbstractNoteBuilder<T extends NoteContainer> implements NoteBuilder<T> {
    private final Map<NominalType, Deque<NominalType>> banknotes;


    protected AbstractNoteBuilder() {
        banknotes = new EnumMap<>(NominalType.class);
    }

    public static Deque<NominalType> collectNotes(NominalType nominalType, int numberOfNotes) {
        var banknotes = new ArrayDeque<NominalType>();
        IntStream.rangeClosed(1, numberOfNotes).forEach(addition -> banknotes.addLast(nominalType));
        return banknotes;
    }

    public AbstractNoteBuilder<T> put5000(int numberOfNotes) {
        banknotes.put(_5000, collectNotes(_5000, numberOfNotes));
        return this;
    }

    public AbstractNoteBuilder<T> put1000(int numberOfNotes) {
        banknotes.put(_1000, collectNotes(_1000, numberOfNotes));
        return this;
    }

    public AbstractNoteBuilder<T> put500(int numberOfNotes) {
        banknotes.put(_500, collectNotes(_500, numberOfNotes));
        return this;
    }

    public AbstractNoteBuilder<T> put100(int numberOfNotes) {
        banknotes.put(NominalType._100, collectNotes(_100, numberOfNotes));
        return this;
    }

    @Override
    public T build() {
        return getInstance(banknotes);
    }

    protected abstract T getInstance(Map<NominalType, Deque<NominalType>> banknotes);
}
