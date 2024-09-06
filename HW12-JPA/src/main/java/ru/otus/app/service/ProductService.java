package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.otus.app.dao.ProductDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Product;

import java.util.List;

@RequiredArgsConstructor
public class ProductService {

    @NonNull
    private final ProductDao productDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Product> findAll() {
        return transactionManager.doInReadOnlyTransaction(productDao::getAll);
    }
}
