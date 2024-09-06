package ru.otus.app.dao;

import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import ru.otus.app.model.Purchase;

import java.util.List;

public class PurchaseDao {

    public List<Purchase> getAll(Session session) {
        return session
                .createQuery(String.format("from %s", Purchase.class.getSimpleName()), Purchase.class)
                .applyGraph(session.getEntityGraph("Purchase.withClientAndProduct"), GraphSemantic.FETCH)
                .getResultList();
    }
}
