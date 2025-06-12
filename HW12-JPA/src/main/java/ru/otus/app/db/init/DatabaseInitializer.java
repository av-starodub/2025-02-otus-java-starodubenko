package ru.otus.app.db.init;

import lombok.RequiredArgsConstructor;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.AbstractBaseEntity;

import java.util.List;

@RequiredArgsConstructor
public class DatabaseInitializer {

    private final TransactionManager transactionManager;

    public <T extends AbstractBaseEntity> void init(List<T> entities) {
        transactionManager.doInTransaction(session -> {
            entities.forEach(session::merge);
            return null;
        });
    }
}
