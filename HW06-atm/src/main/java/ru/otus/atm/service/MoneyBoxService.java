package ru.otus.atm.service;

import ru.otus.atm.container.NoteBox;
import ru.otus.atm.container.NoteContainer;
import ru.otus.atm.exeption.NotEnoughBanknotesException;
import ru.otus.atm.exeption.NotEnoughMoneyException;
import ru.otus.atm.exeption.NotFreeSpaceException;
import ru.otus.atm.exeption.NotValidSumException;
import ru.otus.atm.nominal.NominalType;

import java.util.*;

public class MoneyBoxService implements NoteBoxService {
    private final NoteBox moneyBox;
    private final Set<NominalType> procedureForIssuingLarge;

    public MoneyBoxService(NoteBox noteBox) {
        Objects.requireNonNull(noteBox, " noteBox must not ge null");
        moneyBox = noteBox;
        procedureForIssuingLarge = new TreeSet<>((nominal1, nominal2) -> nominal2.getValue() - nominal1.getValue());
        procedureForIssuingLarge.addAll(List.of(NominalType.values()));
    }

    @Override
    public int putMoney(NoteContainer money) {
        Objects.requireNonNull(money, " money must not be null");
        var notesToAdd = money.getNumberOfNotes();
        var notesInStock = moneyBox.getNumberOfNotes();
        var ceilSize = moneyBox.getCeilSize();
        notesInStock.forEach((nominal, notes) -> {
            if (ceilSize - notes < notesToAdd.get(nominal)) {
                throw new NotFreeSpaceException(" not enough free space");
            }
        });
        moneyBox.putNotes(notesToAdd);
        return checkBalance();
    }

    @Override
    public NoteContainer getMoney(int sum) {
        var minNominal = NominalType.getMinValue();
        var notValidSum = sum < minNominal || sum % minNominal != 0;
        if (notValidSum) {
            throw new NotValidSumException(" the amount must be a multiple " + minNominal);
        }
        if (sum > checkBalance()) {
            throw new NotEnoughMoneyException(" not enough money");
        }
        var notesInStock = moneyBox.getNumberOfNotes();
        var notesRequired = new EnumMap<NominalType, Integer>(NominalType.class);
        var residualAmount = sum;
        for (var nominalType : procedureForIssuingLarge) {
            var currentNominalValue = nominalType.getValue();
            var nominalInStock = notesInStock.get(nominalType);
            var nominalRequired = residualAmount / currentNominalValue;
            if (nominalInStock == 0 || nominalRequired == 0) {
                continue;
            }
            var notesToIssue = nominalInStock > nominalRequired ? nominalRequired : nominalInStock;
            notesRequired.put(nominalType, notesToIssue);
            residualAmount -= notesToIssue * nominalType.getValue();
            if (residualAmount == 0) {
                break;
            }
        }
        if (residualAmount != 0) {
            throw new NotEnoughBanknotesException(" not enough banknotes");
        }
        return moneyBox.extractNotes(notesRequired);
    }

    @Override
    public int checkBalance() {
        return moneyBox.getBalance();
    }
}
