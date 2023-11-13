package ru.otus.atmemulator.nominal;

public enum NominalType {
    _5000(5000),
    _1000(1000),
    _500(500),
    _100(100);

    public final int nominal;

    NominalType(int nominal) {
        this.nominal = nominal;
    }

    public int getValue() {
        return nominal;
    }

    public static int getMinValue() {
        return 100;
    }
}
