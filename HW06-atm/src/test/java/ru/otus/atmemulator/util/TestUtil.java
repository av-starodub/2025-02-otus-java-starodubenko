package ru.otus.atmemulator.util;

import ru.otus.atmemulator.container.Money;
import ru.otus.atmemulator.container.MoneyBox;
import ru.otus.atmemulator.container.NoteBox;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.nominal.NominalType;

import java.util.Map;

public class TestUtil {

    private TestUtil() {
    }

    public static NoteBox createEmptyBox(int ceilSize) {
        return MoneyBox.builder(ceilSize).put5000(0).put1000(0).put500(0).put100(0).build();
    }

    public static Map<NominalType, Integer> createBanknotes(int _5000, int _1000, int _500, int _100) {
        return Map.of(
                NominalType._5000, _5000,
                NominalType._1000, _1000,
                NominalType._500, _500,
                NominalType._100, _100
        );
    }
    public static NoteContainer createMoney(int _5000, int _1000, int _500, int _100) {
        return Money.builder().put5000(_5000).put1000(_1000).put500(_500).put100(_100).build();
    }

}
