package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.dao.ClientDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Client;

import java.util.List;

@RequiredArgsConstructor
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    @NonNull
    private final ClientDao clientDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Client> getAll() {
        return transactionManager.doInReadOnlyTransaction(clientDao::getAll);
    }
}
