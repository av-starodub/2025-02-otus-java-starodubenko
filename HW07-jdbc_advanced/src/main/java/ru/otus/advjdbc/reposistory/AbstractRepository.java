package ru.otus.advjdbc.reposistory;

import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryTable;
import ru.otus.advjdbc.database.dbexecutor.DataBaseOperationExecutor;
import ru.otus.advjdbc.exceptions.AbstractRepositoryException;
import ru.otus.advjdbc.exceptions.DataBaseOperationException;
import ru.otus.advjdbc.model.AbstractBaseEntity;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AbstractRepository<T extends AbstractBaseEntity> {
    private final DataBaseOperationExecutor dbExecutor;

    private final String tableName;
    private final List<String> columnLabelsWithoutId;
    private final List<String> allColumnLabels;
    private final String insertQuery;
    private final String updateQuery;
    private final String findByIdQuery;
    private final String findAllQuery;
    private final String deleteByIdQuery;
    private final String deleteAllQuery;


    private final List<Field> cachedFieldsWithoutId;
    private final List<Field> cachedAllFields;
    private final Constructor<T> constructor;

    public AbstractRepository(DataBaseOperationExecutor executor, Class<T> cls) throws NoSuchFieldException {
        dbExecutor = executor;
        tableName = cls.getAnnotation(RepositoryTable.class).title();
        cachedFieldsWithoutId = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .collect(Collectors.toList());
        var id = cls.getSuperclass().getDeclaredField("id");
        cachedAllFields = new ArrayList<>();
        cachedAllFields.add(id);
        cachedAllFields.addAll(cachedFieldsWithoutId);
        columnLabelsWithoutId = cachedFieldsWithoutId.stream()
                .map(this::getColumnLabel)
                .collect(Collectors.toList());
        allColumnLabels = new ArrayList<>();
        allColumnLabels.add("id");
        allColumnLabels.addAll(columnLabelsWithoutId);
        insertQuery = createInsertQuery();
        updateQuery = createUpdateQuery();
        findByIdQuery = "SELECT * FROM %s WHERE id = ?".formatted(tableName);
        findAllQuery = "SELECT * FROM %s".formatted(tableName);
        deleteByIdQuery = "DELETE FROM %s WHERE id = ?".formatted(tableName);
        deleteAllQuery = "TRUNCATE TABLE %s".formatted(tableName);
        constructor = getConstructor(cls);
    }

    public long create(Connection connection, T entity) {
        try {
            var queryParams = getEntityFieldValuesWithoutId(entity);
            return dbExecutor.executeStatement(connection, insertQuery, queryParams);
        } catch (IllegalAccessException e) {
            throw new AbstractRepositoryException("create entity error ", e);
        }
    }

    public void update(Connection connection, T entity) {
        try {
            var params = getEntityFieldValuesWithoutId(entity);
            params.add(entity.getId());
            dbExecutor.executeStatement(connection, updateQuery, params);
        } catch (IllegalAccessException e) {
            throw new AbstractRepositoryException("update entity error ", e);
        }
    }

    public Optional<T> findById(Connection connection, Long id) {
        return dbExecutor.executeSelect(connection, findByIdQuery, List.of(id), resultSet -> {
            try {
                if (resultSet.next()) {
                    return createEntity(resultSet);
                }
                return null;
            } catch (SQLException e) {
                throw new DataBaseOperationException("findById error ", e);
            }
        });
    }

    public List<T> findAll(Connection connection) {
        return dbExecutor.executeSelect(connection, findAllQuery, List.of(), resultSet -> {
            var entities = new ArrayList<T>();
            try {
                while (resultSet.next()) {
                    entities.add(createEntity(resultSet));
                }
                return entities;
            } catch (SQLException e) {
                throw new DataBaseOperationException("findAll error ", e);
            }
        }).orElseThrow(() -> new AbstractRepositoryException("unexpected error "));
    }

    public boolean deleteById(Connection connection, Long id) {
        return dbExecutor.executeDelete(connection, deleteByIdQuery, List.of(id));
    }

    public boolean deleteAll(Connection connection) {
        return dbExecutor.executeDelete(connection, deleteAllQuery, List.of());
    }

    private T createEntity(ResultSet resultSet) {
        var entityFieldValues = allColumnLabels.stream().map(columnName -> {
            try {
                return resultSet.getObject(columnName);
            } catch (SQLException e) {
                throw new DataBaseOperationException("", e);
            }
        }).toArray();
        try {
            return constructor.newInstance(entityFieldValues);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AbstractRepositoryException("create entity error ", e);
        }
    }

    private Constructor<T> getConstructor(Class<T> cls) {
        var fieldTypes = cachedAllFields.stream()
                .map(Field::getType)
                .toArray(Class[]::new);
        try {
            return cls.getDeclaredConstructor(fieldTypes);
        } catch (NoSuchMethodException e) {
            throw new AbstractRepositoryException("get entity constructor error ", e);
        }
    }

    private List<Object> getEntityFieldValuesWithoutId(T entity) throws IllegalAccessException {
        var fieldValues = new ArrayList<>();
        for (var field : cachedFieldsWithoutId) {
            field.setAccessible(true);
            fieldValues.add(field.get(entity));
            field.setAccessible(false);
        }
        return fieldValues;
    }

    private String createInsertQuery() {
        var columns = String.join(", ", columnLabelsWithoutId);
        var paramsTemplate = columnLabelsWithoutId.stream()
                .map(column -> "?")
                .collect(Collectors.joining(", "));
        return "INSERT INTO %s (%s) VALUES (%s);".formatted(tableName, columns, paramsTemplate);
    }

    private String createUpdateQuery() {
        var columns = columnLabelsWithoutId.stream()
                .map("%s = ?"::formatted)
                .collect(Collectors.joining(", "));
        return "UPDATE %s SET %s WHERE id = ?".formatted(tableName, columns);
    }

    private String getColumnLabel(Field field) {
        var columnLabel = field.getAnnotation(RepositoryField.class).columnName();
        return columnLabel.isEmpty() ? field.getName() : columnLabel;
    }
}
