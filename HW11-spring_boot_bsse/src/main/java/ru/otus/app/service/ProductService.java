package ru.otus.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.model.Product;
import ru.otus.app.repository.ProductDao;

import java.util.List;

@Service
public class ProductService {
    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao dao) {
        productDao = dao;
    }

    public Product create(ProductDto dto) {
        return productDao.insert(new Product(null, dto.getTitle(), dto.getPrice()));
    }

    public Product getById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    public List<Product> getAll() {
        return productDao.findAll();
    }
}
