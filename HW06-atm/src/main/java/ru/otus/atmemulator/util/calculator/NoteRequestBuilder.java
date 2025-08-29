package ru.otus.atmemulator.util.calculator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.atmemulator.denomination.Note;

public final class NoteRequestBuilder {

    private NoteRequestBuilder() {}

    public static Optional<Map<Note, Integer>> buildNoteRequestToIssue(
            Map<Note, Integer> notesInStock, int requiredSum) {
        return buildNoteRequestToIssue(notesInStock, requiredSum, CalculationOrderType.MINIMUM_NOTES);
    }

    public static Optional<Map<Note, Integer>> buildNoteRequestToIssue(
            Map<Note, Integer> notesInStock, int requiredSum, CalculationOrderType calcType) {
        var notesRequired = new HashMap<Note, Integer>();
        var residualAmount = requiredSum;

        if (residualAmount == 0) {
            return Optional.of(Map.of());
        }

        for (var note : calcType.sortByOrderType(notesInStock.keySet())) {
            var currentNominalValue = note.getNominalValue();
            var currentNominalNotesInStock = notesInStock.getOrDefault(note, 0);
            var currentNominalNotesRequired = residualAmount / currentNominalValue;
            var notesToIssue = currentNominalNotesInStock > currentNominalNotesRequired
                    ? currentNominalNotesRequired
                    : currentNominalNotesInStock;
            notesRequired.put(note, notesToIssue);
            residualAmount -= notesToIssue * currentNominalValue;
            if (residualAmount == 0) {
                break;
            }
        }
        return residualAmount == 0 ? Optional.of(notesRequired) : Optional.empty();
    }
}
