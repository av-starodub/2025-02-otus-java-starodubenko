package ru.otus.emulator.util.calculator;

import ru.otus.emulator.nominal.NominalType;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public enum CalculationOrderType {
    MINIMUM_NOTES((nominal1, nominal2) -> nominal2.getValue() - nominal1.getValue());

    private final Set<NominalType> calculationOrder;

    CalculationOrderType(Comparator<NominalType> comparator) {
        calculationOrder = new TreeSet<>(comparator);
        calculationOrder.addAll(List.of(NominalType.values()));
    }

    public Set<NominalType> getCalculationOrder() {
        return calculationOrder;
    }
}
