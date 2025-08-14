package ru.otus.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;
import ru.otus.app.model.Product;
import ru.otus.app.repository.Dao;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final Dao productRepository;

    private final ObjectFactory<Cart> cartFactory;

    public CartService(Dao dao, ObjectFactory<Cart> cartFactory) {
        Objects.requireNonNull(dao, "Parameter dao must not be null");
        this.productRepository = dao;
        this.cartFactory = cartFactory;
    }

    public Product add(long id, Cart cart) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            log.warn("Product with id {} not found", id);
            return null;
        }
        var product = productOptional.get();
        if (cart.addProduct(product)) {
            return product;
        } else {
            log.warn("Cannot add to cart: {}", product);
            return null;
        }
    }

    public boolean remove(long id, Cart cart) {
        return cart.removeProduct(id);
    }

    public List<Product> showCart(Cart cart) {
        return cart.getProducts();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Cart createNewCart() {
        return cartFactory.getObject();
    }
}
