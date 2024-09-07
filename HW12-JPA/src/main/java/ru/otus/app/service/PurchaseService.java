package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.otus.app.dao.PurchaseDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Client;
import ru.otus.app.model.Purchase;

import java.util.List;

@RequiredArgsConstructor
public class PurchaseService {

    @NonNull
    private final PurchaseDao purchaseDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Purchase> getAll() {
        return transactionManager.doInReadOnlyTransaction(purchaseDao::findAll);
    }

    public List<Purchase> getPurchasedProductsByClientId(Long clientId) {
        return transactionManager.doInReadOnlyTransaction(session -> purchaseDao.findAllByClientId(session, clientId)
        );
    }

    public List<Client> getClientsByProductId(Long productId) {
        return transactionManager.doInReadOnlyTransaction(session -> {
                    var clientPurchase = purchaseDao.findAllByProductId(session, productId);
                    return clientPurchase.stream()
                            .map(Purchase::getClient)
                            .distinct()
                            .toList();
                }
        );
    }
}
