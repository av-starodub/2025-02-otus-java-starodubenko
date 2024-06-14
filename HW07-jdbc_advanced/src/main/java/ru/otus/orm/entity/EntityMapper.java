package ru.otus.orm.entity;

import lombok.Getter;
import ru.otus.orm.exceptions.EntityMapperException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class EntityMapper<T extends AbstractBaseEntity> {

    private final EntityMetaData<T> metaData;

    @Getter
    private final String insertQuery;
    @Getter
    private final String updateQuery;
    @Getter
    private final String findByIdQuery;
    @Getter
    private final String findAllQuery;
    @Getter
    private final String deleteByIdQuery;
    @Getter
    private final String deleteAllQuery;

    public EntityMapper(EntityMetaData<T> entityMetaData) {
        metaData = entityMetaData;
        var tableName = metaData.getTableName();
        var columnLabelsWithoutId = metaData.getColumnLabelsWithoutId();
        insertQuery = createInsertQuery(columnLabelsWithoutId, tableName);
        updateQuery = createUpdateQuery(columnLabelsWithoutId, tableName);
        findByIdQuery = "SELECT * FROM %s WHERE id = ?".formatted(tableName);
        findAllQuery = "SELECT * FROM %s".formatted(tableName);
        deleteByIdQuery = "DELETE FROM %s WHERE id = ?".formatted(tableName);
        deleteAllQuery = "TRUNCATE TABLE %s".formatted(tableName);
    }

    private String createInsertQuery(List<String> columnLabelsWithoutId, String tableName) {
        var columns = String.join(", ", columnLabelsWithoutId);
        var paramsTemplate = columnLabelsWithoutId.stream()
                .map(column -> "?")
                .collect(Collectors.joining(", "));
        return "INSERT INTO %s (%s) VALUES (%s);".formatted(tableName, columns, paramsTemplate);
    }

    private String createUpdateQuery(List<String> columnLabelsWithoutId, String tableName) {
        var columns = columnLabelsWithoutId.stream()
                .map("%s = ?"::formatted)
                .collect(Collectors.joining(", "));
        return "UPDATE %s SET %s WHERE id = ?".formatted(tableName, columns);
    }

    public T createEntity(Object[] entityFieldValues) {
        try {
            var constructor = metaData.getConstructor();
            return constructor.newInstance(entityFieldValues);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new EntityMapperException("create entity error ", e);
        }
    }

    public List<Object> extractEntityFieldValuesWithoutId(T entity) {
        var fieldValues = new ArrayList<>();
        for (var field : metaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            try {
                fieldValues.add(field.get(entity));
            } catch (IllegalAccessException e) {
                throw new EntityMapperException("getEntityFieldValuesWithoutId error ", e);
            } finally {
                field.setAccessible(false);
            }
        }
        return fieldValues;
    }

    public List<String> getColumnLabels() {
        return metaData.getColumnLabels();
    }
}
