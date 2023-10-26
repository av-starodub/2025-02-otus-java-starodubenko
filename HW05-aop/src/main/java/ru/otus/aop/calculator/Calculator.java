package ru.otus.aop.calculator;

public interface Calculator {
    int calculate();

    int calculate(int number);

    int calculate(int number1, int number2);

    String calculate(int number1, int number2, String unit);
}
