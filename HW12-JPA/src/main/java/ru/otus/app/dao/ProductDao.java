package ru.otus.app.dao;

import org.hibernate.Session;
import ru.otus.app.model.Product;

import java.util.List;

public class ProductDao {

    public List<Product> getAll(Session session) {
        return session
                .createQuery(String.format("from %s", Product.class.getSimpleName()), Product.class)
                .getResultList();
    }
}
