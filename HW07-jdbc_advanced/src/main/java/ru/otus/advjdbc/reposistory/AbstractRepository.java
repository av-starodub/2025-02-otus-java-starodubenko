package ru.otus.advjdbc.reposistory;


import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryIdField;
import ru.otus.advjdbc.RepositoryTable;
import ru.otus.advjdbc.database.dbexecutor.DataBaseOperationExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private final DataBaseOperationExecutor dbExecutor;

    private final String tableName;
    private final String insertQuery;

    private final List<Field> cachedFieldsWithoutId;
    private final List<Field> cachedAllFields;

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
    }

    public long create(Connection connection, T entity) {
        try {
            var queryParams = getFieldValues(entity);
            return dbExecutor.executeStatement(connection, insertQuery, queryParams);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
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
