package ru.otus.app.repository;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.otus.app.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductDao implements ProductDao {

    private final Map<Long, Product> products;

    private final AtomicLong idGenerator;

    public InMemoryProductDao() {
        products = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong();
    }

    @PostConstruct
    public void init() {
        this.products.put(1L, new Product(1L, "Product 1", 10.0));
        this.products.put(2L, new Product(2L, "Product 2", 20.0));
        this.products.put(3L, new Product(3L, "Product 3", 30.0));
        this.products.put(4L, new Product(4L, "Product 4", 40.0));
        this.products.put(5L, new Product(5L, "Product 5", 50.0));
    }

    @Override
    public Product insert(Product product) {
        var productId = product.getId();
        if (productId == null) {
            productId = idGenerator.incrementAndGet();
            product.setId(productId);
        }

        return products.merge(productId, product, (existingProduct, newProduct) -> {
            existingProduct.setTitle(newProduct.getTitle());
            existingProduct.setPrice(newProduct.getPrice());
            return existingProduct;
        });
    }

    @Override
    public Optional<Product> findById(long productId) {
        return Optional.ofNullable(products.get(productId));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(products.values());
    }
}
