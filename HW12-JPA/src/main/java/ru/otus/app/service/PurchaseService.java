package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.otus.app.dao.PurchaseDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Purchase;

import java.util.List;

@RequiredArgsConstructor
public class PurchaseService {

    @NonNull
    private final PurchaseDao purchaseDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Purchase> getAll() {
        return transactionManager.doInTransaction(purchaseDao::getAll);
    }

}
