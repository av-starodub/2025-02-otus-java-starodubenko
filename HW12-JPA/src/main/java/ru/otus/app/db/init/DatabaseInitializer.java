package ru.otus.app.db.init;

import ru.otus.app.db.sessionmanager.TransactionManager;

import java.util.List;

public class DatabaseInitializer {
    private final TransactionManager transactionManager;

    public DatabaseInitializer(TransactionManager transManager) {
        transactionManager = transManager;
    }

    public void init(List<?> entities) {
        transactionManager.doInTransaction(session -> {
            entities.forEach(session::persist);
            return null;
        });
    }
}
