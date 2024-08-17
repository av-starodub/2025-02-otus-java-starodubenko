package ru.otus.app.repository;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.app.loader.ProductLoader;
import ru.otus.app.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryProductDao implements ProductDao {
    private static final Logger log = LoggerFactory.getLogger(InMemoryProductDao.class);
    private final Map<Long, Product> products;

    private final AtomicLong idGenerator;

    private final ProductLoader productLoader;

    @Autowired
    public InMemoryProductDao(ProductLoader loader) {
        products = new ConcurrentHashMap<>();
        idGenerator = new AtomicLong();
        productLoader = loader;
    }

    @PostConstruct
    public void init() {
        log.info("ProductRepository initialization start...");
        var productsDto = productLoader.loadProducts();
        for (var productDto : productsDto.getProducts()) {
            long id = idGenerator.incrementAndGet();
            var product = productDto.toProduct();
            product.setId(id);
            products.put(id, product);
            log.info("Loaded: {}", product);
        }
        log.info("ProductRepository initialization finished");
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
