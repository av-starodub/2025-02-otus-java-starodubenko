package ru.otus.atmemulator.util.calculator;

import java.util.EnumMap;
import java.util.Map;
import ru.otus.atmemulator.nominal.NominalType;

public final class NoteCalculator {

    private NoteCalculator() {}

    public static Map<NominalType, Integer> compute(Map<NominalType, Integer> notesInStock, int requiredSum) {
        return compute(notesInStock, requiredSum, CalculationOrderType.MINIMUM_NOTES);
    }

    public static Map<NominalType, Integer> compute(
            Map<NominalType, Integer> notesInStock, int requiredSum, CalculationOrderType calcType) {
        var notesRequired = new EnumMap<NominalType, Integer>(NominalType.class);
        var residualAmount = requiredSum;
        for (var nominal : calcType.getCalculationOrder()) {
            var currentNominalValue = nominal.getValue();
            var nominalInStock = notesInStock.get(nominal);
            var nominalRequired = residualAmount / currentNominalValue;
            var notesToIssue = nominalInStock > nominalRequired ? nominalRequired : nominalInStock;
            notesRequired.put(nominal, notesToIssue);
            residualAmount -= notesToIssue * currentNominalValue;
            if (residualAmount == 0) {
                break;
            }
        }
        return residualAmount == 0 ? notesRequired : null;
    }
}
