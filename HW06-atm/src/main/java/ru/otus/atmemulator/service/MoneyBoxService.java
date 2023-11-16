package ru.otus.atmemulator.service;

import ru.otus.atmemulator.container.NoteBox;
import ru.otus.atmemulator.container.NoteContainer;
import ru.otus.atmemulator.exeption.NotEnoughBanknotesException;
import ru.otus.atmemulator.exeption.NotFreeSpaceException;
import ru.otus.atmemulator.nominal.NominalType;

import java.util.*;

public class MoneyBoxService implements NoteBoxService {
    private final NoteBox moneyBox;
    private final Set<NominalType> calculationOrder;

    public MoneyBoxService(NoteBox noteBox) {
        Objects.requireNonNull(noteBox, " noteBox must not ge null");
        moneyBox = noteBox;
        calculationOrder = new TreeSet<>((nominal1, nominal2) -> nominal2.value - nominal1.value);
        calculationOrder.addAll(List.of(NominalType.values()));
    }

    @Override
    public int putMoney(NoteContainer money) {
        Objects.requireNonNull(money, " money must not be null");
        var notesToAdd = money.getNumberOfNotes();
        var notesInStock = moneyBox.getNumberOfNotes();
        var ceilSize = moneyBox.getCeilSize();
        notesInStock.forEach((nominal, stackSize) -> {
            var stackSizeToAdd = notesToAdd.get(nominal);
            if (ceilSize - stackSize < stackSizeToAdd) {
                throw new NotFreeSpaceException(" not enough free space");
            }
        });
        return moneyBox.putNotes(money);
    }

    @Override
    public NoteContainer getMoney(int requiredSum) {
        var notesInStock = moneyBox.getNumberOfNotes();
        var notesRequired = createRequest(notesInStock, requiredSum);
        if (Objects.isNull(notesRequired)) {
            throw new NotEnoughBanknotesException(" not enough banknotes");
        }
        return moneyBox.extractNotes(notesRequired);
    }

    @Override
    public int checkBalance() {
        return moneyBox.getAmount();
    }

    private Map<NominalType, Integer> createRequest(Map<NominalType, Integer> notesInStock, int requiredSum) {
        var notesRequired = new EnumMap<NominalType, Integer>(NominalType.class);
        var residualAmount = requiredSum;
        for (var nominal : calculationOrder) {
            var currentNominalValue = nominal.value;
            var nominalInStock = notesInStock.get(nominal);
            var nominalRequired = residualAmount / currentNominalValue;
            if (nominalInStock == 0 || nominalRequired == 0) {
                continue;
            }
            var notesToIssue = nominalInStock > nominalRequired ? nominalRequired : nominalInStock;
            notesRequired.put(nominal, notesToIssue);
            residualAmount -= notesToIssue * nominal.value;
            if (residualAmount == 0) {
                break;
            }
        }
        return residualAmount == 0 ? notesRequired : null;
    }
}
