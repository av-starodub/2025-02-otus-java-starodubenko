package ru.otus.atm;

import ru.otus.atm.service.NoteBoxService;

import java.util.Objects;

public class ATM {
    private final NoteBoxService noteBoxService;

    public ATM(NoteBoxService noteBoxService) {
        Objects.requireNonNull(noteBoxService, " noteBoxService must not ge null");
        this.noteBoxService = noteBoxService;
    }
}
