package ru.otus.atm.service;

import ru.otus.atm.container.Money;
import ru.otus.atm.container.NoteBox;
import ru.otus.atm.container.NoteContainer;

import java.util.Objects;

public class MoneyBoxService implements NoteBoxService {
    private final NoteBox noteBox;

    public MoneyBoxService(NoteBox noteBox) {
        Objects.requireNonNull(noteBox, " noteBox must not ge null");
        this.noteBox = noteBox;
    }

    @Override
    public int putMoney(NoteContainer money) {
        return 0;
    }

    @Override
    public Money getMoney(int sum) {
        return null;
    }

    @Override
    public int checkBalance() {
        return 0;
    }
}
