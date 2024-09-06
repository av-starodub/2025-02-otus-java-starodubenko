package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.dao.PurchaseDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Purchase;

import java.util.List;

@RequiredArgsConstructor
public class PurchaseService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseService.class);

    @NonNull
    private final PurchaseDao purchaseDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Purchase> getAll() {
        return transactionManager.doInTransaction(session -> {
            var purchaseList = purchaseDao.getAll(session);
            log.info("purchaseList {}", purchaseList);
            return purchaseList;
        });
    }

}
