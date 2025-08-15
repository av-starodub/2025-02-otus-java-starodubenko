package ru.otus.atmemulator.atm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.exeption.*;
import ru.otus.atmemulator.nominal.NominalType;
import ru.otus.atmemulator.service.MoneyBoxService;
import ru.otus.atmemulator.service.NoteBoxService;
import ru.otus.atmemulator.testutil.TestUtil;

class ATMTest {

    private static final int REQUIRED_SUM = 100;

    private static final String EXPECTED_ERROR_MESSAGE = "Error. Operation can be performed";

    private ATM atm;

    private NoteBoxService noteBoxService;

    @BeforeEach
    void setUp() {
        noteBoxService = Mockito.mock(MoneyBoxService.class);
        atm = new ATM(noteBoxService);
    }

    @Test
    @DisplayName("When not enough free space should throw AtmException")
    void checkThrowWhenNotFreeSpace() {
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
    @DisplayName("When required sum less than minNominal should throw AtmException")
    void checkThrowWhenRequiredSumLessThanMinNominal() {
        int tooLessSum = 7;

        Throwable thrown = catchThrowable(() -> atm.getMoney(tooLessSum));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotValidSumException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("the amount must be a multiple");
    }

    @Test
    @DisplayName("When required sum not a multiple of the minNominal should throw AtmException")
    void checkThrowWhenRequiredSumNotMultipleMinNominal() {
        int notMultipleSum = NominalType.getMinValue() + 1;

        Throwable thrown = catchThrowable(() -> atm.getMoney(notMultipleSum));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotValidSumException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("the amount must be a multiple");
    }

    @Test
    @DisplayName("When sum more than balance should throw AtmException")
    void checkThrowWhenNotEnoughMoney() {
        when(noteBoxService.checkBalance()).thenReturn(0);

        Throwable thrown = catchThrowable(() -> atm.getMoney(REQUIRED_SUM));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotEnoughMoneyException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("not enough money");
    }

    @Test
    @DisplayName("When not enough banknotes should throw AtmException")
    void checkThrowWhenNotEnoughBanknotes() {
        when(noteBoxService.checkBalance()).thenReturn(REQUIRED_SUM + NominalType.getMinValue());
        when(noteBoxService.getMoney(anyInt())).thenThrow(new NotEnoughBanknotesException(" not enough banknotes"));

        Throwable thrown = catchThrowable(() -> atm.getMoney(REQUIRED_SUM));

        assertThat(thrown)
                .isInstanceOf(AtmException.class)
                .hasRootCauseInstanceOf(NotEnoughBanknotesException.class)
                .hasMessageContaining(EXPECTED_ERROR_MESSAGE)
                .hasMessageContaining("not enough banknotes");
    }
}
