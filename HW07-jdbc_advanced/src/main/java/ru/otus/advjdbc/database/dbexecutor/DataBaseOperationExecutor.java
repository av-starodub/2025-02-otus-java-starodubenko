package ru.otus.advjdbc.database.dbexecutor;

import ru.otus.advjdbc.exceptions.DataBaseOperationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class DataBaseOperationExecutor {
    private static final int PRIMARY_KEY_COLUMN_INDEX = 1;

    public long executeStatement(Connection connection, String sqlQuery, List<Object> queryParams) {
        checkArgs(sqlQuery, queryParams);

        try (var prepareStatement =
                     connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            for (var idx = 0; idx < queryParams.size(); idx++) {
                prepareStatement.setObject(idx + 1, queryParams.get(idx));
            }
            prepareStatement.executeUpdate();

            try (var resultSet = prepareStatement.getGeneratedKeys()) {
                resultSet.next();
                return resultSet.getLong(PRIMARY_KEY_COLUMN_INDEX);
            }

        } catch (SQLException e) {
            throw new DataBaseOperationException("executeStatement error", e);
        }
    }

    public <T> Optional<T> executeSelect(
            Connection connection, String sqlQuery, List<Object> queryParams, Function<ResultSet, T> rsHandler) {
        checkArgs(sqlQuery, queryParams);
        requireNonNull(rsHandler, "the ResultSet handler function must not be null ");

        try (var preparedStatement = connection.prepareStatement(sqlQuery)) {
            for (var idx = 0; idx < queryParams.size(); idx++) {
                preparedStatement.setObject(idx + 1, queryParams.get(idx));
            }
            try (var resultSet = preparedStatement.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(resultSet));
            }
        } catch (SQLException e) {
            throw new DataBaseOperationException("executeSelect error", e);
        }
    }

    public boolean executeDelete(Connection connection, String sqlQuery, List<Object> queryParams) {
        try (var preparedStatement =
                     connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

            for (var idx = 0; idx < queryParams.size(); idx++) {
                preparedStatement.setObject(idx + 1, queryParams.get(idx));
            }

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataBaseOperationException("executeDelete error", e);
        }
    }

    private void checkArgs(String sqlQuery, List<Object> queryParams) {
        requireNonNull(sqlQuery, "sql-query must not be null ");
        requireNonNull(queryParams, "the list with sql-query parameters can be empty, but not null ");
        if (sqlQuery.isEmpty()) {
            throw new IllegalArgumentException("sql query must not be empty ");
        }
    }
}
