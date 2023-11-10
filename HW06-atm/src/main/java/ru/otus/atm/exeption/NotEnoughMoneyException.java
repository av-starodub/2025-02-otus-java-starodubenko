package ru.otus.atm.exeption;

public class NotEnoughMoneyException extends AtmException {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
