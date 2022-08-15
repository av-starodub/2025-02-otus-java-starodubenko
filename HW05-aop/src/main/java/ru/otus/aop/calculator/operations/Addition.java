package ru.otus.aop.calculator.operations;

import ru.otus.aop.calculator.Calculator;

public class Addition implements Calculator {
    @Override
    public int calculate(int number) {
        return number;
    }

    @Override
    public int calculate(int number1, int number2) {
        return number1 + number2;
    }

    @Override
    public String calculate(int number1, int number2, String unit) {
        return (number1 + number2) + unit;
    }
}
