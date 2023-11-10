package ru.otus.atm.service;

import ru.otus.atm.container.NoteContainer;

public interface NoteBoxService {
    int putMoney(NoteContainer money);

    NoteContainer getMoney(int sum);

    int checkBalance();
}
