package ru.otus.orm.reposistory;

import ru.otus.orm.database.dbexecutor.DataBaseOperationExecutor;
import ru.otus.orm.entity.AbstractBaseEntity;
import ru.otus.orm.entity.EntityMapper;
import ru.otus.orm.exceptions.DataBaseOperationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public final class AbstractRepository<T extends AbstractBaseEntity> {

    private final DataBaseOperationExecutor dbExecutor;

    private final EntityMapper<T> entityMapper;

    public AbstractRepository(DataBaseOperationExecutor executor, EntityMapper<T> mapper) {
        requireNonNull(executor, "Parameter executor must not be null");
        requireNonNull(mapper, "Parameter mapper must not be null");
        dbExecutor = executor;
        entityMapper = mapper;
    }

    public long create(Connection connection, T entity) {
        requireNonNull(entity, "Parameter entity must not be null");
        var queryParams = entityMapper.extractEntityFieldValuesWithoutId(entity);
        return dbExecutor.executeStatement(connection, entityMapper.getInsertQuery(), queryParams);
    }

    public void update(Connection connection, T entity) {
        requireNonNull(entity, "Parameter entity must not be null");
        var params = entityMapper.extractEntityFieldValuesWithoutId(entity);
        params.add(entity.getId());
        dbExecutor.executeStatement(connection, entityMapper.getUpdateQuery(), params);
    }

    public Optional<T> findById(Connection connection, Long id) {
        requireNonNull(id, "Parameter id must not be null");
        return dbExecutor.executeSelect(
                connection, entityMapper.getFindByIdQuery(), List.of(id), this::mapResultSetToEntity
        );
    }

    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, entityMapper.getFindAllQuery(), List.of(), resultSet -> {
            var entities = new ArrayList<T>();
            try {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
                return entities;
            } catch (SQLException e) {
                throw new DataBaseOperationException("Failed to handle ResultSet", e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    public boolean deleteById(Connection connection, Long id) {
        requireNonNull(id, "Parameter id must not be null");
        return dbExecutor.executeDelete(connection, entityMapper.getDeleteByIdQuery(), List.of(id));
    }

    public boolean deleteAll(Connection connection) {
        return dbExecutor.executeDelete(connection, entityMapper.getDeleteAllQuery(), List.of());
    }

    private Object[] getEntityFieldValues(ResultSet resultSet) {
        return entityMapper.getColumnLabels().stream()
                .map(columnLabel -> {
                    try {
                        return resultSet.getObject(columnLabel);
                    } catch (SQLException e) {
                        throw new DataBaseOperationException("Failed to get entity field values", e);
                    }
                }).toArray();
    }

    private T mapResultSetToEntity(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                var args = getEntityFieldValues(resultSet);
                return entityMapper.createEntity(args);
            }
            return null;
        } catch (SQLException e) {
            throw new DataBaseOperationException("Failed to map ResultSet to entity ", e);
        }
    }
}
