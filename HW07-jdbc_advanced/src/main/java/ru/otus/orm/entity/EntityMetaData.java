package ru.otus.orm.entity;

import lombok.Getter;
import ru.otus.orm.entity.annotations.RepositoryField;
import ru.otus.orm.entity.annotations.RepositoryTable;
import ru.otus.orm.exceptions.AbstractRepositoryException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class EntityMetaData<T extends AbstractBaseEntity> {

    private final String tableName;

    private final List<Field> fieldsWithoutId;

    private final List<Field> fields;

    private final Constructor<T> constructor;

    private final List<String> columnLabelsWithoutId;

    private final List<String> columnLabels;


    public EntityMetaData(Class<T> cls) throws NoSuchFieldException {
        tableName = cls.getAnnotation(RepositoryTable.class).title();
        fields = new ArrayList<>();
        fieldsWithoutId = new ArrayList<>();
        columnLabelsWithoutId = new ArrayList<>();
        columnLabels = new ArrayList<>();
        fill(fields, fieldsWithoutId, columnLabels, columnLabelsWithoutId, cls);
        constructor = getConstructor(cls);
    }

    private void fill(
            List<Field> fields,
            List<Field> fieldsWithoutId,
            List<String> labels, List<String> labelsWithoutId,
            Class<T> cls)
            throws NoSuchFieldException {
        fields.add(AbstractBaseEntity.class.getDeclaredField("id"));
        labels.add("id");
        for (var field : cls.getDeclaredFields()) {
            fields.add(field);
            fieldsWithoutId.add(field);
            var columnLabel = getColumnLabel(field);
            labels.add(columnLabel);
            labelsWithoutId.add(columnLabel);
        }
    }

    private Constructor<T> getConstructor(Class<T> cls) {
        var fieldTypes = fields.stream()
                .map(Field::getType)
                .toArray(Class[]::new);
        try {
            return cls.getDeclaredConstructor(fieldTypes);
        } catch (NoSuchMethodException e) {
            throw new AbstractRepositoryException("get entity constructor error ", e);
        }
    }

    private String getColumnLabel(Field field) {
        var columnLabel = field.getAnnotation(RepositoryField.class).columnName();
        return columnLabel.isEmpty() ? field.getName() : columnLabel;
    }
}
