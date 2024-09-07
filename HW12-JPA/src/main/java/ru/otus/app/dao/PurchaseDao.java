package ru.otus.app.dao;

import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;
import ru.otus.app.model.Purchase;

import java.util.List;

public class PurchaseDao {

    public List<Purchase> findAll(Session session) {
        return session
                .createQuery("from Purchase", Purchase.class)
                .applyGraph(session.getEntityGraph("Purchase.withClientAndProduct"), GraphSemantic.FETCH)
                .getResultList();
    }

    public List<Purchase> findAllByClientId(Session session, Long clientId) {
        return session
                .createQuery("FROM Purchase p WHERE p.client.id = :clientId", Purchase.class)
                .setParameter("clientId", clientId)
                .applyGraph(session.getEntityGraph("Purchase.withClientAndProduct"), GraphSemantic.FETCH)
                .getResultList();
    }

    public List<Purchase> findAllByProductId(Session session, Long productId) {
        return session
                .createQuery("FROM Purchase p WHERE p.product.id = :productId", Purchase.class)
                .setParameter("productId", productId)
                .applyGraph(session.getEntityGraph("Purchase.withClientAndProduct"), GraphSemantic.FETCH)
                .getResultList();
    }

}
