package ru.otus.app.repository;

import ru.otus.app.model.Product;

import java.util.List;
import java.util.Optional;

public interface Dao {

    void init();

    Optional<Product> findById(Long id);

    List<Product> findAll();

}
