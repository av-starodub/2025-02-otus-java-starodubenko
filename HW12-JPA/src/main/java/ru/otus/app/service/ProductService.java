package ru.otus.app.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.dao.ProductDao;
import ru.otus.app.model.Product;
import ru.otus.app.db.sessionmanager.TransactionManager;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductDao productDao;

    private final TransactionManager transactionManager;

    public List<Product> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var productList = productDao.getAll(session);
            log.info("productList {}", productList);
            return productList;
        });
    }
}
