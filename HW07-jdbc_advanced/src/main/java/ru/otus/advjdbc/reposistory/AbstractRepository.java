package ru.otus.advjdbc.reposistory;


import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryIdField;
import ru.otus.advjdbc.RepositoryTable;
import ru.otus.advjdbc.database.dbexecutor.DataBaseOperationExecutor;
import ru.otus.advjdbc.exceptions.AbstractRepositoryException;
import ru.otus.advjdbc.exceptions.DataBaseOperationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private final DataBaseOperationExecutor dbExecutor;

    private final String tableName;
    private final String insertQuery;
    private final String findAllQuery;

    private final List<Field> cachedFieldsWithoutId;
    private final List<Field> cachedAllFields;
    private final Constructor<T> constructor;

    public AbstractRepository(DataBaseOperationExecutor executor, Class<T> cls) {
        dbExecutor = executor;
        tableName = cls.getAnnotation(RepositoryTable.class).title();
        cachedAllFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .collect(Collectors.toList());
        cachedFieldsWithoutId = cachedAllFields.stream()
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .toList();
        insertQuery = createInsertQuery(cls);
        findAllQuery = "SELECT * FROM %s".formatted(tableName);
        constructor = getConstructor(cls);
    }

    public long create(Connection connection, T entity) {
        try {
            var queryParams = getFieldValues(entity);
            return dbExecutor.executeStatement(connection, insertQuery, queryParams);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
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
                throw new DataBaseOperationException("findAll error", e);
            }
        }).orElseThrow(() -> new RuntimeException("unexpected error"));
    }

    private T createEntity(ResultSet resultSet) {
        var entityFieldValues = cachedAllFields.stream().map(field -> {
            var columnName = getColumnName(field);
            try {
                return resultSet.getObject(columnName);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }).toArray();
        try {
            return constructor.newInstance(entityFieldValues);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new AbstractRepositoryException("create entity error", e);
        }
    }

    private Constructor<T> getConstructor(Class<T> cls) {
        var fieldTypes = cachedAllFields.stream()
                .map(Field::getType)
                .toArray(Class[]::new);
        try {
            return cls.getDeclaredConstructor(fieldTypes);
        } catch (NoSuchMethodException e) {
            throw new AbstractRepositoryException("get entity constructor error", e);
        }
    }

    private List<Object> getFieldValues(T entity) throws IllegalAccessException {
        var fieldValues = new ArrayList<>();
        for (var field : cachedFieldsWithoutId) {
            field.setAccessible(true);
            fieldValues.add(field.get(entity));
            field.setAccessible(false);
        }
        return fieldValues;
    }

    private String createInsertQuery(Class<T> cls) {
        var query = new StringBuilder("insert into ");
        query.append(tableName).append(" (");
        for (var f : cachedFieldsWithoutId) {
            var columnName = getColumnName(f);
            query.append(columnName).append(", ");
        }
        // 'insert into users (login, password, nickname, '
        query.setLength(query.length() - 2);
        // 'insert into users (login, password, nickname'
        query.append(") values (");
        for (var f : cachedFieldsWithoutId) {
            query.append("?, ");
        }
        // 'insert into users (login, password, nickname) values (?, ?, ?, '
        query.setLength(query.length() - 2);
        // 'insert into users (login, password, nickname) values (?, ?, ?'
        query.append(");");
        return query.toString();
    }

    private String getColumnName(Field field) {
        var annotationNameValue = field.getAnnotation(RepositoryField.class).columnName();
        return annotationNameValue.isEmpty() ? field.getName() : annotationNameValue;
    }
}
