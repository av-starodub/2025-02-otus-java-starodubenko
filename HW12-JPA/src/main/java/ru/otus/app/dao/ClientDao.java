package ru.otus.app.dao;

import org.hibernate.Session;
import ru.otus.app.model.Client;

import java.util.List;

import static java.util.Objects.nonNull;

public class ClientDao {

    public List<Client> getAll(Session session) {
        return session
                .createQuery(String.format("from %s", Client.class.getSimpleName()), Client.class)
                .getResultList();
    }

    public boolean deleteByIdIfExist(Session session, Long productId) {
        var client = session.get(Client.class, productId);
        if (nonNull(client)) {
            session.delete(client);
            return true;
        } else {
            return false;
        }
    }
}
