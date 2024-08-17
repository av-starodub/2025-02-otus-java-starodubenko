package ru.otus.app.repository;

import ru.otus.app.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {
    Product insert(Product product);
    
    Optional<Product> findById(long id);

    List<Product> findAll();
}
