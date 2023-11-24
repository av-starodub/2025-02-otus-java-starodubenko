package ru.otus.atmemulator.service;

import ru.otus.atmemulator.container.NoteContainer;

public interface NoteBoxService {
    int putMoney(NoteContainer money);

    NoteContainer getMoney(int sum);

    int checkBalance();
}
