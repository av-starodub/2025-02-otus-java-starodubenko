package ru.otus.atmemulator.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.exeption.*;
import ru.otus.atmemulator.service.MoneyBoxService;
import ru.otus.atmemulator.service.NoteBoxService;
import ru.otus.atmemulator.util.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class ATMTest {
    private static final int REQUIRED_SUM = 100;
    private static final String EXPECTED_ERROR_MESSAGE = "Error. Operation can be performed";
    private ATM atm;
    private NoteBoxService noteBoxService;

    @BeforeEach
    public void setUp() {
        noteBoxService = Mockito.mock(MoneyBoxService.class);
        atm = new ATM(noteBoxService);
    }

    @Test
    @DisplayName("When not enough free space should throw AtmException")
    public void checkThrowWhenNotFreeSpace() {
        when(noteBoxService.putMoney(any(NoteContainer.class)))
                .thenThrow(new NotFreeSpaceException(" not enough free space"));
        var money = TestUtil.createMoney(1, 1, 5, 1);

        Throwable thrown = catchThrowable(() -> atm.putMoney(money));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotFreeSpaceException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("not enough free space");
    }

    @Test
    @DisplayName("When sum incorrectly should throw AtmException")
    public void checkThrowWhenRequiredSumIncorrectly() {
        when(noteBoxService.getMoney(anyInt()))
                .thenThrow(new NotValidSumException(" the amount must be a multiple"));

        Throwable thrown = catchThrowable(() -> atm.getMoney(REQUIRED_SUM));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotValidSumException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("the amount must be a multiple");
    }

    @Test
    @DisplayName("When sum more than balance should throw AtmException")
    public void checkThrowWhenNotEnoughMoney() {
        when(noteBoxService.getMoney(anyInt()))
                .thenThrow(new NotEnoughMoneyException(" not enough money"));

        Throwable thrown = catchThrowable(() -> atm.getMoney(REQUIRED_SUM));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotEnoughMoneyException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("not enough money");
    }

    @Test
    @DisplayName("When not enough banknotes should throw AtmException")
    public void checkThrowWhenNotEnoughBanknotes() {
        when(noteBoxService.getMoney(anyInt()))
                .thenThrow(new NotEnoughBanknotesException(" not enough banknotes"));

        Throwable thrown = catchThrowable(() -> atm.getMoney(REQUIRED_SUM));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotEnoughBanknotesException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("not enough banknotes");
    }
}
