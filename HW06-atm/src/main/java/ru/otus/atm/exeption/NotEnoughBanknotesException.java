package ru.otus.atm.exeption;

public class NotEnoughBanknotesException extends AtmException {
    public NotEnoughBanknotesException(String message) {
        super(message);
    }
}
