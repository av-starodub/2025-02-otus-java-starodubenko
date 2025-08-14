package ru.otus.app.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.app.loader.ProductLoader;
import ru.otus.app.model.Product;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductRepository implements Dao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryProductRepository.class);

    private final ConcurrentHashMap<Long, Product> productMap = new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong();

    private final ProductLoader productLoader;

    @Autowired
    public InMemoryProductRepository(ProductLoader loader) {
        Objects.requireNonNull(loader, "Parameter loader must not be null");
        this.productLoader = loader;
    }

    @Override
    public void init() {
        log.info("ProductRepository initialisation start...");
        var products = productLoader.loadProducts();
        for (var product : products) {
            long id = idGenerator.incrementAndGet();
            product.setId(id);
            productMap.put(id, product);
            log.info("Loaded: {}", product);
        }
        log.info("ProductRepository initialisation finished");
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(productMap.get(id));
    }

    @Override
    public List<Product> findAll() {
        return List.copyOf(productMap.values());
    }
}
