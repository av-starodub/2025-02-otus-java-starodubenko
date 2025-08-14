package ru.otus.crm.service;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> dataTemplate) {
        Objects.requireNonNull(transactionManager, "Parameter transactionManager must not be null");
        Objects.requireNonNull(dataTemplate, "Parameter dataTemplate must not be null");
        this.transactionManager = transactionManager;
        this.clientDataTemplate = dataTemplate;
    }


    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            clientOptional.ifPresent(DbServiceClientImpl::accept);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            clientList.forEach(DbServiceClientImpl::accept);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }

    private static void accept(Client client) {
        Hibernate.initialize(client.getAddress());
        Hibernate.initialize(client.getPhones());
        client.setAddress(Hibernate.unproxy(client.getAddress(), Address.class));
    }
}
