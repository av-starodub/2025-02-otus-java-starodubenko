package ru.otus.atmemulator.util.calculator;

import ru.otus.atmemulator.nominal.NominalType;

import java.util.EnumMap;
import java.util.Map;

public final class NoteCalculator {
    private NoteCalculator() {
    }

    public static Map<NominalType, Integer> compute(Map<NominalType, Integer> notesInStock, int requiredSum) {
        return compute(notesInStock, requiredSum, CalculationOrderType.MINIMUM_NOTES);
    }

    public static Map<NominalType, Integer> compute(
            Map<NominalType, Integer> notesInStock, int requiredSum, CalculationOrderType calcType) {
        var notesRequired = new EnumMap<NominalType, Integer>(NominalType.class);
        var residualAmount = requiredSum;
        for (var nominal : calcType.getCalculationOrder()) {
            var currentNominalValue = nominal.value;
            var nominalInStock = notesInStock.get(nominal);
            var nominalRequired = residualAmount / currentNominalValue;
            if (nominalInStock == 0 || nominalRequired == 0) {
                continue;
            }
            var notesToIssue = nominalInStock > nominalRequired ? nominalRequired : nominalInStock;
            notesRequired.put(nominal, notesToIssue);
            residualAmount -= notesToIssue * nominal.value;
            if (residualAmount == 0) {
                break;
            }
        }
        return residualAmount == 0 ? notesRequired : null;
    }
}
