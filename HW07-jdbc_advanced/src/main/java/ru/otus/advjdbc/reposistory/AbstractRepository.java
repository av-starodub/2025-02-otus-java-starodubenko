package ru.otus.advjdbc.reposistory;


import ru.otus.advjdbc.RepositoryField;
import ru.otus.advjdbc.RepositoryIdField;
import ru.otus.advjdbc.RepositoryTable;
import ru.otus.advjdbc.exceptions.ApplicationInitializationException;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AbstractRepository<T> {
    private final DataSource dataSource;

    private final String insertQuery;

    private List<Field> cachedFields;

    public AbstractRepository(DataSource dataSource, Class<T> cls) {
        this.dataSource = dataSource;
        insertQuery = createInsertQuery(cls);
    }

    public void create(T entity) {
        try (var connection = dataSource.getConnection();
        var pst = connection.prepareStatement(insertQuery)) {
            try {
                for (int i = 0; i < cachedFields.size(); i++) {
                    pst.setObject(i + 1, cachedFields.get(i).get(entity));
                }
                pst.executeUpdate();
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new ApplicationInitializationException();
            }
        } catch (Exception e) {
            throw new ApplicationInitializationException();
        }
    }

    private String createInsertQuery(Class<T> cls) {
        StringBuilder query = new StringBuilder("insert into ");
        String tableName = cls.getAnnotation(RepositoryTable.class).title();
        query.append(tableName).append(" (");
        // 'insert into users ('
        cachedFields = Arrays.stream(cls.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(RepositoryField.class))
                .filter(f -> !f.isAnnotationPresent(RepositoryIdField.class))
                .collect(Collectors.toList());
        for (Field f : cachedFields) { // TODO Сделать использование геттеров
            f.setAccessible(true);
        }
        for (Field f : cachedFields) {
            query.append(f.getName()).append(", ");
        }
        // 'insert into users (login, password, nickname, '
        query.setLength(query.length() - 2);
        // 'insert into users (login, password, nickname'
        query.append(") values (");
        for (Field ignored : cachedFields) {
            query.append("?, ");
        }
        // 'insert into users (login, password, nickname) values (?, ?, ?, '
        query.setLength(query.length() - 2);
        // 'insert into users (login, password, nickname) values (?, ?, ?'
        query.append(");");
        return query.toString();
    }
}
