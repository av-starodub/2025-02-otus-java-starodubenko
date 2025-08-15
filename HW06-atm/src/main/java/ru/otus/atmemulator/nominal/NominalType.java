package ru.otus.atmemulator.nominal;

public enum NominalType {
    RUB_5000(5000),
    RUB_1000(1000),
    RUB_500(500),
    RUB_100(100);

    private final int value;

    NominalType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int getMinValue() {
        return RUB_100.value;
    }
}
