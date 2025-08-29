package ru.otus.atmemulator.container.builder;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.denomination.Note;

/**
 * An abstract base class for building instances of {@link NoteContainer} implementations.
 * Provides methods to manage and assemble collections of {@link Note} into a container.
 * This class uses the builder pattern to facilitate the construction of complex objects.
 *
 * @param <T> the type of {@link NoteContainer} being built
 */
public abstract class AbstractNoteContainerBuilder<T extends NoteContainer> implements NoteContainerBuilder<T> {

    private final Map<Note, Deque<Note>> banknotes;

    protected AbstractNoteContainerBuilder() {
        banknotes = new HashMap<>();
    }

    public static Deque<Note> collectNotes(Note note, int numberOfNotes) {
        var stack = new ArrayDeque<Note>();
        IntStream.rangeClosed(1, numberOfNotes).forEach(addition -> stack.addLast(note));
        return stack;
    }

    public AbstractNoteContainerBuilder<T> put(Note nominal, int numberOfNotes) {
        banknotes.merge(nominal, collectNotes(nominal, numberOfNotes), (existing, add) -> {
            existing.addAll(add);
            return existing;
        });
        return this;
    }

    public AbstractNoteContainerBuilder<T> putAll(Map<Note, Integer> notes) {
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
