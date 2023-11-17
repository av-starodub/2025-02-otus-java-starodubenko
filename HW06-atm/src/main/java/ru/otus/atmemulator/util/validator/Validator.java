package ru.otus.atmemulator.util.validator;

import ru.otus.atmemulator.exeption.NotEnoughMoneyException;
import ru.otus.atmemulator.exeption.NotValidSumException;
import ru.otus.atmemulator.nominal.NominalType;

public final class Validator {
    private Validator() {
    }

    public static void checkSum(int requiredSum, int balance) {
        var minNominal = NominalType.getMinValue();
        if (requiredSum < minNominal || requiredSum % minNominal != 0) {
            throw new NotValidSumException(" the amount must be a multiple " + minNominal);
        }
        if (requiredSum > balance) {
            throw new NotEnoughMoneyException(" not enough money");
        }
    }
}
