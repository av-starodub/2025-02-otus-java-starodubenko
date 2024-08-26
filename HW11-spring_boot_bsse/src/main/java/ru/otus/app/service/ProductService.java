package ru.otus.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.dto.ProductsDto;
import ru.otus.app.exception.ProductNotFoundException;
import ru.otus.app.model.Product;
import ru.otus.app.repository.ProductDao;

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

    public ProductDto getById(Long id) {
        requireNonNull(id, "Product ID must not be null");
        Product product = productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID=%d".formatted(id)));
        return new ProductDto(product.getTitle(), product.getPrice());
    }

    public ProductsDto getAll() {
        var products = productDao.findAll();
        var dtos = products.stream()
                .map(product -> new ProductDto(product.getTitle(), product.getPrice()))
                .toList();
        return new ProductsDto(dtos);
    }
}
