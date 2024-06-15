package ru.otus.orm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.orm.database.dbtransaction.TransactionExecutor;
import ru.otus.orm.entity.AbstractBaseEntity;
import ru.otus.orm.reposistory.AbstractRepository;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public final class AbstractRepositoryService<T extends AbstractBaseEntity> {
    private final static Logger LOG = LoggerFactory.getLogger(AbstractRepositoryService.class);

    private final AbstractRepository<T> dao;

    private final TransactionExecutor executor;

    public AbstractRepositoryService(AbstractRepository<T> repository, TransactionExecutor tExec) {
        this.dao = repository;
        this.executor = tExec;
    }

    public T save(T entity) {
        requireNonNull(entity, "parameter entity must not be null ");
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
        requireNonNull(id, "parameter id must not be null ");
        var entityOptional = executor.executeTransaction(connection -> dao.findById(connection, id));
        LOG.info("required entityOptional: {}", entityOptional);
        return entityOptional;
    }

    public List<T> getAll() {
        var entities = executor.executeTransaction(dao::findAll);
        LOG.info("list of entities: {}", entities);
        return entities;
    }

    public boolean remove(Long id) {
        requireNonNull(id, "parameter id must not be null ");
        var isEntityRemoved = executor.executeTransaction(connection -> dao.deleteById(connection, id));
        LOG.info("entity with id={} removed: {}", id, isEntityRemoved);
        return isEntityRemoved;
    }

    public boolean removeAll() {
        var isEntitiesRemoved = executor.executeTransaction(dao::deleteAll);
        LOG.info("all entities removed: {}", isEntitiesRemoved);
        return isEntitiesRemoved;
    }
}
