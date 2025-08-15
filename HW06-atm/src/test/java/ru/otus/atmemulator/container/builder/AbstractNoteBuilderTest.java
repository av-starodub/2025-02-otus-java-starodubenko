package ru.otus.atmemulator.container.builder;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.atmemulator.container.NoteBox;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.container.money.Money;
import ru.otus.atmemulator.container.moneybox.MoneyBox;
import ru.otus.atmemulator.nominal.NominalType;
import ru.otus.atmemulator.testutil.TestUtil;

class AbstractNoteBuilderTest {

    private static final Map<NominalType, Integer> EXPECTED_NOTES = TestUtil.createBanknotes(1, 1, 1, 1);

    private static final int EXPECTED_BALANCE = 6600;

    @Test
    @DisplayName("Check that builder creates the Money instance correctly")
    void buildMoneyTest() {
        var money = Money.builder().put5000(1).put1000(1).put500(1).put100(1).build();

        var actualNotes = money.getNumberOfNotes();
        var actualBalance = money.getAmount();

        assertThat(money).isInstanceOf(NoteContainer.class).isNotInstanceOf(NoteBox.class);
        assertThat(actualBalance).isEqualTo(EXPECTED_BALANCE);
        assertThat(actualNotes).containsExactlyInAnyOrderEntriesOf(EXPECTED_NOTES);
    }

    @Test
    @DisplayName("Check that builder creates the MoneyBox instance correctly")
    void buildMoneyBoxTest() {
        var moneyBox =
                MoneyBox.builder(1).put5000(1).put1000(1).put500(1).put100(1).build();

        var actualNotes = moneyBox.getNumberOfNotes();
        var actualBalance = moneyBox.getAmount();

        assertThat(moneyBox).isInstanceOf(NoteContainer.class).isInstanceOf(NoteBox.class);
        assertThat(actualBalance).isEqualTo(EXPECTED_BALANCE);
        assertThat(actualNotes).containsExactlyInAnyOrderEntriesOf(EXPECTED_NOTES);
    }
}
