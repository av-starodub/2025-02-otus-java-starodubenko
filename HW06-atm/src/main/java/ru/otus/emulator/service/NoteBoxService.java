package ru.otus.emulator.service;

import ru.otus.emulator.container.NoteContainer;

public interface NoteBoxService {
    int putMoney(NoteContainer money);

    NoteContainer getMoney(int sum);

    int checkBalance();
}
