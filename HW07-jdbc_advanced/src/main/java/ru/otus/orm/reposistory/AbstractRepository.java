package ru.otus.orm.reposistory;

import ru.otus.orm.database.dbexecutor.DataBaseOperationExecutor;
import ru.otus.orm.entity.AbstractBaseEntity;
import ru.otus.orm.entity.EntityMapper;
import ru.otus.orm.exceptions.AbstractRepositoryException;
import ru.otus.orm.exceptions.DataBaseOperationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class AbstractRepository<T extends AbstractBaseEntity> {

    private final DataBaseOperationExecutor dbExecutor;

    private final EntityMapper<T> entityMapper;

    public AbstractRepository(DataBaseOperationExecutor executor, EntityMapper<T> mapper) {
        dbExecutor = executor;
        entityMapper = mapper;
    }

    public long create(Connection connection, T entity) {
        var queryParams = entityMapper.extractEntityFieldValuesWithoutId(entity);
        return dbExecutor.executeStatement(connection, entityMapper.getInsertQuery(), queryParams);
    }

    public void update(Connection connection, T entity) {
        var params = entityMapper.extractEntityFieldValuesWithoutId(entity);
        params.add(entity.getId());
        dbExecutor.executeStatement(connection, entityMapper.getUpdateQuery(), params);
    }

    public Optional<T> findById(Connection connection, Long id) {
        return dbExecutor.executeSelect(connection, entityMapper.getFindByIdQuery(), List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    var args = getConstructorArgs(resultSet);
                    return entityMapper.createEntity(args);
                }
                return null;
            } catch (SQLException e) {
                throw new DataBaseOperationException("findById error ", e);
            }
        });
    }

    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entityMapper.getFindAllQuery(), List.of(), resultSet -> {
            var entities = new ArrayList<T>();
            try {
                while (resultSet.next()) {
                    var args = getConstructorArgs(resultSet);
                    entities.add(entityMapper.createEntity(args));
                }
                return entities;
            } catch (SQLException e) {
                throw new DataBaseOperationException("findAll error ", e);
            }
        }).orElseThrow(() -> new AbstractRepositoryException("unexpected error "));
    }

    public boolean deleteById(Connection connection, Long id) {
        return dbExecutor.executeDelete(connection, entityMapper.getDeleteByIdQuery(), List.of(id));
    }

    public boolean deleteAll(Connection connection) {
        return dbExecutor.executeDelete(connection, entityMapper.getDeleteAllQuery(), List.of());
    }

    private Object[] getConstructorArgs(ResultSet resultSet) {
        return entityMapper.getColumnLabels().stream()
                .map(columnName -> {
                    try {
                        return resultSet.getObject(columnName);
                    } catch (SQLException e) {
                        throw new DataBaseOperationException("getConstructorArgs error", e);
                    }
                }).toArray();
    }
}
