package ru.otus.emulator.container.builder;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.emulator.nominal.NominalType;
import ru.otus.emulator.container.money.Money;
import ru.otus.emulator.container.moneybox.MoneyBox;
import ru.otus.emulator.container.NoteBox;
import ru.otus.emulator.container.NoteContainer;
import ru.otus.emulator.testutil.TestUtil;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractNoteBuilderTest {
    private final static Map<NominalType, Integer> EXPECTED_NOTES = TestUtil.createBanknotes(1, 1, 1, 1);
    private static final int EXPECTED_BALANCE = 6600;

    @Test
    @DisplayName("Check that builder creates the Money instance correctly")
    public void buildMoneyTest() {
        var money = Money.builder().put5000(1).put1000(1).put500(1).put100(1).build();

        var actualNotes = money.getNumberOfNotes();
        var actualBalance = money.getAmount();

        assertThat(money).isInstanceOf(NoteContainer.class);
        assertThat(money).isNotInstanceOf(NoteBox.class);
        assertThat(actualBalance).isEqualTo(EXPECTED_BALANCE);
        assertThat(actualNotes).containsExactlyInAnyOrderEntriesOf(EXPECTED_NOTES);
    }

    @Test
    @DisplayName("Check that builder creates the MoneyBox instance correctly")
    public void buildMoneyBoxTest() {
        var moneyBox = MoneyBox.builder(1).put5000(1).put1000(1).put500(1).put100(1).build();

        var actualNotes = moneyBox.getNumberOfNotes();
        var actualBalance = moneyBox.getAmount();

        assertThat(moneyBox).isInstanceOf(NoteContainer.class);
        assertThat(moneyBox).isInstanceOf(NoteBox.class);
        assertThat(actualBalance).isEqualTo(EXPECTED_BALANCE);
        assertThat(actualNotes).containsExactlyInAnyOrderEntriesOf(EXPECTED_NOTES);
    }
}
