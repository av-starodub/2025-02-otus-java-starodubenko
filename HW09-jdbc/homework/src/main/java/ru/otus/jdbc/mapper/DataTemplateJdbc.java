package ru.otus.jdbc.mapper;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;
import ru.otus.jdbc.mapper.excrption.EntityClassMetaDataException;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;

    private final EntitySQLMetaData entitySQLMetaData;

    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(
            DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createEntity(rs);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var entityList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            entityList.add(createEntity(rs));
                        }
                        return entityList;
                    } catch (Exception e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            var fieldValues = getFieldValues(client);
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), fieldValues);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), getFieldValues(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private Object getColumn(ResultSet rs, Field field) {
        try {
            return rs.getObject(field.getName());
        } catch (SQLException e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldValues(T entity) throws IllegalAccessException {
        try {
            var beanInfo = Introspector.getBeanInfo(entity.getClass());
            var readersByName = Arrays.stream(beanInfo.getPropertyDescriptors())
                    .filter(pd -> pd.getReadMethod() != null)
                    .collect(Collectors.toMap(PropertyDescriptor::getName, PropertyDescriptor::getReadMethod));

            var fieldValues = new ArrayList<>();
            for (var field : entityClassMetaData.getFieldsWithoutId()) {
                var getter = readersByName.get(field.getName());
                if (getter == null) {
                    throw new EntityClassMetaDataException("No public getter for field: " + field.getName());
                }
                fieldValues.add(getter.invoke(entity));
            }
            return fieldValues;
        } catch (IntrospectionException | InvocationTargetException e) {
            throw new DataTemplateException(e);
        }
    }

    private T createEntity(ResultSet rs)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        var fields = entityClassMetaData.getAllFields();
        var entityFieldValues =
                fields.stream().map(field -> getColumn(rs, field)).toArray();
        var entityConstructor = entityClassMetaData.getConstructor();
        return entityConstructor.newInstance(entityFieldValues);
    }
}
