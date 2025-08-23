package ru.otus.atmemulator.container.builder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.denomination.Note;

public abstract class AbstractNoteBuilder<T extends NoteContainer> implements NoteBuilder<T> {

    private final Map<Note, Deque<Note>> banknotes;

    protected AbstractNoteBuilder() {
        banknotes = new HashMap<>();
    }

    public static Deque<Note> collectNotes(Note note, int numberOfNotes) {
        var stack = new ArrayDeque<Note>();
        IntStream.rangeClosed(1, numberOfNotes).forEach(addition -> stack.addLast(note));
        return stack;
    }

    public AbstractNoteBuilder<T> put(Note nominal, int numberOfNotes) {
        banknotes.merge(nominal, collectNotes(nominal, numberOfNotes), (existing, add) -> {
            existing.addAll(add);
            return existing;
        });
        return this;
    }

    public AbstractNoteBuilder<T> putAll(Map<Note, Integer> notes) {
        if (notes == null || notes.isEmpty()) {
            return this;
        }
        notes.forEach((nominal, count) -> put(nominal, count == null ? 0 : count));
        return this;
    }

    @Override
    public T build() {
        return getInstance(banknotes);
    }

    protected abstract T getInstance(Map<Note, Deque<Note>> banknotes);
}
