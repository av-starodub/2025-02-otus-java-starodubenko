package ru.otus.emulator.util.validator;

import ru.otus.emulator.exeption.NotEnoughMoneyException;
import ru.otus.emulator.exeption.NotValidSumException;
import ru.otus.emulator.nominal.NominalType;

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
