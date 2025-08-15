package ru.otus.atmemulator.testutil;

import java.util.Map;
import ru.otus.atmemulator.container.NoteBox;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.container.money.Money;
import ru.otus.atmemulator.container.moneybox.MoneyBox;
import ru.otus.atmemulator.nominal.NominalType;

public class TestUtil {

    private TestUtil() {}

    public static NoteBox createEmptyBox(int ceilSize) {
        return MoneyBox.builder(ceilSize)
                .put5000(0)
                .put1000(0)
                .put500(0)
                .put100(0)
                .build();
    }

    public static Map<NominalType, Integer> createBanknotes(int r5000, int r1000, int r500, int r100) {
        return Map.of(
                NominalType.RUB_5000, r5000,
                NominalType.RUB_1000, r1000,
                NominalType.RUB_500, r500,
                NominalType.RUB_100, r100);
    }

    public static NoteContainer createMoney(int r5000, int r1000, int r500, int r100) {
        return Money.builder()
                .put5000(r5000)
                .put1000(r1000)
                .put500(r500)
                .put100(r100)
                .build();
    }
}
