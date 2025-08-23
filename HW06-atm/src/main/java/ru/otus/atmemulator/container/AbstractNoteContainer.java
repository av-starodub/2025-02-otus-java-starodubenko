package ru.otus.atmemulator.container;

import java.util.*;
import java.util.stream.Collectors;
import ru.otus.atmemulator.denomination.Note;

public abstract class AbstractNoteContainer implements NoteContainer {

    protected final Map<Note, Deque<Note>> banknotes;

    protected AbstractNoteContainer(Map<Note, Deque<Note>> banknotes) {
        Objects.requireNonNull(banknotes, "Parameter banknotes must not be null");
        this.banknotes = new HashMap<>(banknotes);
    }

    @Override
    public Map<Note, Integer> getNumberOfNotes() {
        return banknotes.entrySet().stream().collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, e -> e.getValue()
                .size()));
    }

    protected static int computeAmount(Map<Note, Deque<Note>> banknotes) {
        return banknotes.entrySet().stream()
                .mapToInt(entry ->
                        entry.getKey().getNominalValue() * entry.getValue().size())
                .sum();
    }
}
