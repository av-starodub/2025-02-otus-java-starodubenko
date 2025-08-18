package ru.otus.atmemulator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static ru.otus.atmemulator.nominal.NominalType.*;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atmemulator.exeption.NotEnoughBanknotesException;
import ru.otus.atmemulator.exeption.NotFreeSpaceException;
import ru.otus.atmemulator.testutil.TestUtil;

class MoneyBoxServiceTest {

    private MoneyBoxService moneyBoxService;

    @BeforeEach
    void setUp() {
        var moneyBox = TestUtil.createEmptyBox(4);
        moneyBoxService = new MoneyBoxService(moneyBox);
    }

    @Test
    @DisplayName("Check that putMoney() adds money correctly")
    void checkPositiveScriptOfPutMoney() {
        var money = TestUtil.createMoney(1, 1, 1, 1);
        moneyBoxService.putMoney(money);

        var expectedBalance = money.getAmount();
        var actualBalance = moneyBoxService.checkBalance();

        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    @DisplayName("When not enough free space should throw NotFreeSpaceException")
    void checkThrowWhenNotFreeSpace() {
        var money = TestUtil.createMoney(1, 1, 5, 1);

        Throwable thrown = catchThrowable(() -> moneyBoxService.putMoney(money));

        var actualBalance = moneyBoxService.checkBalance();

        assertThat(thrown).isInstanceOf(NotFreeSpaceException.class).hasMessageContaining("not enough free space");
        assertThat(actualBalance).isZero();
    }

    @Test
    @DisplayName("Check that getMoney() extracts money correctly")
    void checkPositiveScriptOfGetMoney() {
        int requiredSum = 6000;
        var money = TestUtil.createMoney(1, 0, 4, 1);
        moneyBoxService.putMoney(money);

        var actualMoney = moneyBoxService.getMoney(requiredSum);

        var actualBalance = moneyBoxService.checkBalance();
        var expectedBalance = 1100;
        var actualBanknotes = actualMoney.getNumberOfNotes();
        var expectedBanknotes = Map.of(RUB_5000, 1, RUB_500, 2);

        assertThat(actualBalance).isEqualTo(expectedBalance);
        assertThat(actualBanknotes).containsExactlyInAnyOrderEntriesOf(expectedBanknotes);
    }

    @Test
    @DisplayName("When not enough banknotes should throw NotEnoughBanknotesException")
    void checkThrowWhenNotEnoughBanknotes() {
        var money = TestUtil.createMoney(1, 1, 0, 1);
        moneyBoxService.putMoney(money);
        int requiredSum = 500;
        Throwable thrown = catchThrowable(() -> moneyBoxService.getMoney(requiredSum));
        assertThat(thrown).isInstanceOf(NotEnoughBanknotesException.class).hasMessageContaining("not enough banknotes");
    }
}
