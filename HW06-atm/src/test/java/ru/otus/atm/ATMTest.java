package ru.otus.atm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ATMTest {

    @Test
    @DisplayName("Check disbursement of cash balance")
    public void getBalanceTest() {
        var atm = new ATM();
        assertThat(0).isEqualTo(atm.getBalance());
    }
}
