package ru.otus.atmemulator.builder;

import ru.otus.atmemulator.nominal.NominalType;
import ru.otus.atmemulator.container.NoteContainer;

import java.util.EnumMap;
import java.util.Map;

public abstract class AbstractNoteBuilder<T extends NoteContainer> implements NoteBuilder<T> {
    private final Map<NominalType, Integer> banknotes;

    protected AbstractNoteBuilder() {
        banknotes = new EnumMap<>(NominalType.class);
    }

    public AbstractNoteBuilder<T> put5000(int number) {
        banknotes.put(NominalType._5000, number);
        return this;
    }

    public AbstractNoteBuilder<T> put1000(int number) {
        banknotes.put(NominalType._1000, number);
        return this;
    }

    public AbstractNoteBuilder<T> put500(int number) {
        banknotes.put(NominalType._500, number);
        return this;
    }

    public AbstractNoteBuilder<T> put100(int number) {
        banknotes.put(NominalType._100, number);
        return this;
    }

    @Override
    public T build() {
        return instanceOf(banknotes);
    }

    protected abstract T instanceOf(Map<NominalType, Integer> banknotes);
}
