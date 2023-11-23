package ru.otus.emulator.exeption;

public class NotEnoughBanknotesException extends AtmException {
    public NotEnoughBanknotesException(String message) {
        super(message);
    }
}
