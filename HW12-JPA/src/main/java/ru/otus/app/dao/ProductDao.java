package ru.otus.app.dao;

import org.hibernate.Session;
import ru.otus.app.model.Product;

import java.util.List;

import static java.util.Objects.nonNull;

public class ProductDao {

    public List<Product> getAll(Session session) {
        return session
                .createQuery(String.format("from %s", Product.class.getSimpleName()), Product.class)
                .getResultList();
    }

    public boolean deleteByIdIfExist(Session session, Long productId) {
        var product = session.get(Product.class, productId);
        if (nonNull(product)) {
            session.delete(product);
            return true;
        } else {
            return false;
        }
    }
}
