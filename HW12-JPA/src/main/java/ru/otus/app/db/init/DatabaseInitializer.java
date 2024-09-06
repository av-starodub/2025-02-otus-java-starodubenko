package ru.otus.app.db.init;

import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.AbstractBaseEntity;

import java.util.List;

public class DatabaseInitializer {
    private final TransactionManager transactionManager;

    public DatabaseInitializer(TransactionManager transManager) {
        transactionManager = transManager;
    }

    public <T extends AbstractBaseEntity> void init(List<T> entities) {
        transactionManager.doInTransaction(session -> {
            entities.forEach(session::merge);
            return null;
        });
    }
}
