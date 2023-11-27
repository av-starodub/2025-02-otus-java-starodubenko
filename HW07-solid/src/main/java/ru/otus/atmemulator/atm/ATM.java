package ru.otus.atmemulator.atm;

import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.exeption.AtmException;
import ru.otus.atmemulator.service.NoteBoxService;
import ru.otus.atmemulator.util.validator.Validator;

import java.util.Objects;

public class ATM {
    private final NoteBoxService noteBoxService;

    public ATM(NoteBoxService noteBoxService) {
        Objects.requireNonNull(noteBoxService, " noteBoxService must not be null");
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
            Validator.checkSum(requiredSum, noteBoxService.checkBalance());
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
