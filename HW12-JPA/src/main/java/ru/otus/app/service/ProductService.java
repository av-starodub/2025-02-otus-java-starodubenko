package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.dao.ProductDao;
import ru.otus.app.dao.PurchaseDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Product;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @NonNull
    private final ProductDao productDao;

    @NonNull
    private final PurchaseDao purchaseDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Product> findAll() {
        return transactionManager.doInReadOnlyTransaction(productDao::getAll);
    }

    public boolean deleteByIdIfExist(Long id) {
        return transactionManager.doInTransaction(session -> {
            purchaseDao.deleteAllByProductId(session, id);
            if (productDao.deleteByIdIfExist(session, id)) {
                return true;
            } else {
                log.debug("Product with id={} not exist", id);
                return false;
            }
        });
    }
}
