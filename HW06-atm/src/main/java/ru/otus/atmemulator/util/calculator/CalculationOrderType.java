package ru.otus.atmemulator.util.calculator;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import ru.otus.atmemulator.nominal.NominalType;

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
