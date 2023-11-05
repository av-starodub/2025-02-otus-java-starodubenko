package ru.otus.atm.service;

import ru.otus.atm.container.Money;
import ru.otus.atm.container.NoteContainer;

public interface NoteBoxService {
    int putMoney(NoteContainer money);

    Money getMoney(int sum);

    int checkBalance();
}
