package ru.otus.atmemulator.util.calculator;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import ru.otus.atmemulator.denomination.Note;

public enum CalculationOrderType {
    MINIMUM_NOTES((nominal1, nominal2) -> nominal2.getNominalValue() - nominal1.getNominalValue());

    private final Comparator<Note> noteComparator;

    CalculationOrderType(Comparator<Note> comparator) {
        noteComparator = comparator;
    }

    public Set<Note> sortByOrderType(Set<Note> notes) {
        return notes.stream().sorted(noteComparator).collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
