package ru.otus.app.service;

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

    private final ClientDao clientDao;

    private final TransactionManager transactionManager;

    public List<Client> getAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDao.getAll(session);
            log.info("clientList {}", clientList);
            return clientList;
        });
    }
}
