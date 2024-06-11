package ru.otus.advjdbc.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.advjdbc.database.dbtransaction.TransactionExecutor;
import ru.otus.advjdbc.model.AbstractBaseEntity;
import ru.otus.advjdbc.reposistory.AbstractRepository;

import java.util.Optional;

import static java.util.Objects.isNull;

public class AbstractRepositoryService<T extends AbstractBaseEntity> {
    private final static Logger LOG = LoggerFactory.getLogger(AbstractRepositoryService.class);

    private final AbstractRepository<T> dao;

    private final TransactionExecutor executor;

    public AbstractRepositoryService(AbstractRepository<T> repository, TransactionExecutor tExec) {
        this.dao = repository;
        this.executor = tExec;
    }

    public T save(T entity) {
        return executor.executeTransaction(connection -> {
            if (isNull(entity.getId())) {
                var savedEntityId = dao.create(connection, entity);
                entity.setId(savedEntityId);
                LOG.info("created entity: {}", entity);
                return entity;
            }
            dao.update(connection, entity);
            LOG.info("updated entity: {}", entity);
            return entity;
        });
    }

    public Optional<T> get(Long id) {
        var entityOptional = executor.executeTransaction(connection -> dao.findById(connection, id));
        LOG.info("required entityOptional: {}", entityOptional);
        return entityOptional;
    }
}
