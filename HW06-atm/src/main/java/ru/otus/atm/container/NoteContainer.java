package ru.otus.atm.container;

import ru.otus.atm.nominal.NominalType;

import java.util.Map;

public interface NoteContainer {

    int getBalance();

    /**
     * @return must return unmodifiable map
     */
    Map<NominalType, Integer> getInfo();
}
