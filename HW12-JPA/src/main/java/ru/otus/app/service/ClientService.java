package ru.otus.app.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.dao.ClientDao;
import ru.otus.app.dao.PurchaseDao;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.model.Client;

import java.util.List;

@RequiredArgsConstructor
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);

    @NonNull
    private final ClientDao clientDao;

    @NonNull
    private final PurchaseDao purchaseDao;

    @NonNull
    private final TransactionManager transactionManager;

    public List<Client> getAll() {
        return transactionManager.doInReadOnlyTransaction(clientDao::getAll);
    }

    public boolean deleteByIdIfExist(Long id) {
        return transactionManager.doInTransaction(session -> {
            purchaseDao.deleteAllByClientId(session, id);
            if (clientDao.deleteByIdIfExist(session, id)) {
                return true;
            } else {
                log.debug("Client with id={} not exist", id);
                return false;
            }
        });
    }
}
