package ru.otus.emulator.service;

import ru.otus.emulator.container.NoteBox;
import ru.otus.emulator.container.NoteContainer;
import ru.otus.emulator.exeption.NotEnoughBanknotesException;
import ru.otus.emulator.exeption.NotFreeSpaceException;
import ru.otus.emulator.util.calculator.NoteCalculator;

import java.util.Objects;

public class MoneyBoxService implements NoteBoxService {
    private final NoteBox moneyBox;

    public MoneyBoxService(NoteBox noteBox) {
        Objects.requireNonNull(noteBox, " noteBox must not ge null");
        moneyBox = noteBox;
    }

    @Override
    public int putMoney(NoteContainer money) {
        Objects.requireNonNull(money, " money must not be null");
        var notesToAdd = money.getNumberOfNotes();
        var notesInStock = moneyBox.getNumberOfNotes();
        var ceilSize = moneyBox.getCeilSize();
        notesInStock.forEach((nominal, stackSize) -> {
            var stackSizeToAdd = notesToAdd.get(nominal);
            if (ceilSize - stackSize < stackSizeToAdd) {
                throw new NotFreeSpaceException(" not enough free space");
            }
        });
        return moneyBox.putNotes(money);
    }

    @Override
    public NoteContainer getMoney(int requiredSum) {
        var notesInStock = moneyBox.getNumberOfNotes();
        var notesRequired = NoteCalculator.compute(notesInStock, requiredSum);
        if (Objects.isNull(notesRequired)) {
            throw new NotEnoughBanknotesException(" not enough banknotes");
        }
        return moneyBox.extractNotes(notesRequired);
    }

    @Override
    public int checkBalance() {
        return moneyBox.getAmount();
    }
}
