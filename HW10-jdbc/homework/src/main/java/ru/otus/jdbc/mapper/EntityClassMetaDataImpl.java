package ru.otus.jdbc.mapper;

import ru.otus.crm.model.annotation.Id;
import ru.otus.jdbc.mapper.excrption.EntityClassMetaDataException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String className;
    private final Constructor<T> constructor;
    private final Field id;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        className = clazz.getSimpleName();
        id = getFieldAnnotatedId(clazz);
        allFields = List.of(clazz.getDeclaredFields());
        fieldsWithoutId = allFields.stream().filter(field -> !id.equals(field)).collect(Collectors.toList());
        constructor = getConstructor(clazz);
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return id;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

    private Constructor<T> getConstructor(Class<T> clazz) {
        var fieldTypes = allFields.stream()
                .map(Field::getType)
                .toArray(Class[]::new);
        try {
            return clazz.getDeclaredConstructor(fieldTypes);
        } catch (NoSuchMethodException e) {
            throw new EntityClassMetaDataException(e);
        }
    }

    private Field getFieldAnnotatedId(Class<T> clazz) {
        for (var field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        throw new EntityClassMetaDataException(" no @Id annotated field");
    }
}
