package ru.otus.atm.atm;

import ru.otus.atm.container.NoteContainer;
import ru.otus.atm.exeption.AtmException;
import ru.otus.atm.service.NoteBoxService;

import java.util.Objects;

public class ATM {
    private final NoteBoxService noteBoxService;

    public ATM(NoteBoxService noteBoxService) {
        Objects.requireNonNull(noteBoxService, " noteBoxService must not ge null");
        this.noteBoxService = noteBoxService;
    }

    public int putMoney(NoteContainer money) {
        Objects.requireNonNull(money, " money must not be null");
        try {
            return noteBoxService.putMoney(money);
        } catch (Exception e) {
            throw new AtmException(createErrorMessage(e.getMessage()), e);
        }
    }

    public NoteContainer getMoney(int requiredSum) {
        try {
            return noteBoxService.getMoney(requiredSum);
        } catch (Exception e) {
            throw new AtmException(createErrorMessage(e.getMessage()), e);
        }
    }

    public int checkBalance() {
        return noteBoxService.checkBalance();
    }

    private String createErrorMessage(String message) {
        return (String.format("Error. Operation can be performed, %s", message));
    }
}
