package ru.otus.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.app.model.Product;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class Cart {
    private final List<Product> products;

    @Autowired
    public Cart() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean removeProduct(long id) {
        return products.removeIf(product -> product.getId() == id);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }
}
