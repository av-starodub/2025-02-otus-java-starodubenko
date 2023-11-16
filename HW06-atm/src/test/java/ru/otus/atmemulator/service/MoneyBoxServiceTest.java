package ru.otus.atmemulator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atmemulator.exeption.NotEnoughBanknotesException;
import ru.otus.atmemulator.exeption.NotEnoughMoneyException;
import ru.otus.atmemulator.exeption.NotFreeSpaceException;
import ru.otus.atmemulator.exeption.NotValidSumException;
import ru.otus.atmemulator.nominal.NominalType;
import ru.otus.atmemulator.util.TestUtil;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static ru.otus.atmemulator.nominal.NominalType.*;

public class MoneyBoxServiceTest {
    private MoneyBoxService moneyBoxService;

    @BeforeEach
    void setUp() {
        var moneyBox = TestUtil.createEmptyBox(4);
        moneyBoxService = new MoneyBoxService(moneyBox);
    }

    @Test
    @DisplayName("Check that putMoney() adds money correctly")
    public void checkPositiveScriptOfPutMoney() {
        var money = TestUtil.createMoney(1, 1, 1, 1);
        moneyBoxService.putMoney(money);

        var expectedBalance = money.getAmount();
        var actualBalance = moneyBoxService.checkBalance();

        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("When not enough free space should throw NotFreeSpaceException")
    public void checkThrowWhenNotFreeSpace() {
        var money = TestUtil.createMoney(1, 1, 5, 1);

        Throwable thrown = catchThrowable(() -> moneyBoxService.putMoney(money));

        var actualBalance = moneyBoxService.checkBalance();

        assertThat(thrown)
                .isInstanceOf(NotFreeSpaceException.class)
                .hasMessageContaining("not enough free space");
        assertThat(actualBalance).isEqualTo(0);
    }

    @Test
    @DisplayName("Check that getMoney() extracts money correctly")
    public void checkPositiveScriptOfGetMoney() {
        int requiredSum = 6000;
        var money = TestUtil.createMoney(1, 0, 4, 1);
        moneyBoxService.putMoney(money);

        var actualMoney = moneyBoxService.getMoney(requiredSum);

        var actualBalance = moneyBoxService.checkBalance();
        var expectedBalance = 1100;
        var actualBanknotes = actualMoney.getNumberOfNotes();
        var expectedBanknotes = Map.of(_5000, 1, _1000, 0, _500, 2, _100, 0);

        assertThat(actualBalance).isEqualTo(expectedBalance);
        assertThat(actualBanknotes).containsExactlyInAnyOrderEntriesOf(expectedBanknotes);
    }

    @Test
    @DisplayName("When sum incorrectly should throw NotValidSumException")
    public void checkThrowWhenRequiredSumIncorrectly() {
        int requiredSum = 150;
        Throwable thrown = catchThrowable(() -> moneyBoxService.getMoney(requiredSum));
        assertThat(thrown)
                .isInstanceOf(NotValidSumException.class)
                .hasMessageContaining("the amount must be a multiple " + NominalType.getMinValue());
    }

    @Test
    @DisplayName("When sum more than balance should throw NotEnoughMoneyException")
    public void checkThrowWhenNotEnoughMoney() {
        int requiredSum = 100;
        Throwable thrown = catchThrowable(() -> moneyBoxService.getMoney(requiredSum));
        assertThat(thrown)
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessageContaining("not enough money");
    }

    @Test
    @DisplayName("When not enough banknotes should throw NotEnoughBanknotesException")
    public void checkThrowWhenNotEnoughBanknotes() {
        var money = TestUtil.createMoney(1, 1, 0, 1);
        moneyBoxService.putMoney(money);
        int requiredSum = 500;
        Throwable thrown = catchThrowable(() -> moneyBoxService.getMoney(requiredSum));
        assertThat(thrown)
                .isInstanceOf(NotEnoughBanknotesException.class)
                .hasMessageContaining("not enough banknotes");
    }
}
