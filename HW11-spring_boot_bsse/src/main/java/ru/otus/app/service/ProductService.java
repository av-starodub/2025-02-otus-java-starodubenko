package ru.otus.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.model.Product;
import ru.otus.app.repository.ProductDao;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class ProductService {
    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao dao) {
        requireNonNull(dao, "ProductDao must not be null");
        productDao = dao;
    }

    public Product create(ProductDto dto) {
        requireNonNull(dto, "ProductDto must not be null");
        return productDao.insert(new Product(null, dto.getTitle(), dto.getPrice()));
    }

    public Optional<Product> getById(Long id) {
        requireNonNull(id, "Product ID must not be null");
        return productDao.findById(id);
    }

    public List<Product> getAll() {
        return productDao.findAll();
    }
}
