package ru.otus.aop.calculator.operations;

import ru.otus.aop.calculator.Calculator;
import ru.otus.aop.proxy.annotations.Log;

public class Addition implements Calculator {
    @Override
    @Log
    public int calculate() {
        return 0;
    }

    @Override
    public int calculate(int number) {
        return number;
    }

    @Override
    @Log
    public int calculate(int number1, int number2) {
        return number1 + number2;
    }

    @Override
    @Log
    public String calculate(int number1, int number2, String unit) {
        return (number1 + number2) + unit;
    }
}
