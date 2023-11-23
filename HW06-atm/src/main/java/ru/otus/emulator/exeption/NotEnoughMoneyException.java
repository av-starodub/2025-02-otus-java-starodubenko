package ru.otus.emulator.exeption;

public class NotEnoughMoneyException extends AtmException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
