package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {

    private final String className;

    private final String fieldNameAnnotatedId;

    private final String allFieldNames;

    private final String allFieldNamesWithoutId;

    private final String insertParameters;

    private final String updateColumns;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        className = entityClassMetaData.getName();
        fieldNameAnnotatedId = entityClassMetaData.getIdField().getName();
        allFieldNames = createFieldsTemplate(entityClassMetaData.getAllFields(), Field::getName);
        allFieldNamesWithoutId = createFieldsTemplate(entityClassMetaData.getFieldsWithoutId(), Field::getName);
        insertParameters = createInsertParametersTemplate(entityClassMetaData.getFieldsWithoutId());
        updateColumns = createFieldsTemplate(
                entityClassMetaData.getFieldsWithoutId(), (field -> String.format("%s = ?", field.getName())));
    }

    @Override
    public String getSelectAllSql() {
        return String.format("select * from %s", className);
    }

    @Override
    public String getSelectByIdSql() {
        return String.format("select %s from %s where %s = ?", allFieldNames, className, fieldNameAnnotatedId);
    }

    @Override
    public String getInsertSql() {
        return String.format("insert into %s(%s) values (%s)", className, allFieldNamesWithoutId, insertParameters);
    }

    @Override
    public String getUpdateSql() {
        return String.format("update %s set %s where %s = ?", className, updateColumns, fieldNameAnnotatedId);
    }

    private String createInsertParametersTemplate(List<Field> fieldsWithoutId) {
        return String.join(", ", Collections.nCopies(fieldsWithoutId.size(), "?"));
    }

    private String createFieldsTemplate(List<Field> fieldsWithoutId, Function<Field, String> mapper) {
        return fieldsWithoutId.stream().map(mapper).collect(Collectors.joining(", "));
    }
}
