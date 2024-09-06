package ru.otus.app.dao;

import org.hibernate.Session;
import ru.otus.app.model.Client;

import java.util.List;

public class ClientDao {

    public List<Client> getAll(Session session) {
        return session
                .createQuery(String.format("from %s",Client.class.getSimpleName()), Client.class)
                .getResultList();
    }
}
