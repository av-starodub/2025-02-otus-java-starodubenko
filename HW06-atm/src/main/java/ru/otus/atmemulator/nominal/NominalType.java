package ru.otus.atmemulator.nominal;

public enum NominalType {
    _5000(5000),
    _1000(1000),
    _500(500),
    _100(100);

    public final int value;

    NominalType(int value) {
        this.value = value;
    }

    public static int getMinValue() {
        return _100.value;
    }
}
