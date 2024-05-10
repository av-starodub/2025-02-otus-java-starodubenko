package ru.otus.jdbcproxy.executors;

import ru.otus.jdbcproxy.exception.DataBaseOperationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class DataBaseOperationExecutor {

    private static final int PRIMARY_KEY_COLUMN_INDEX = 1;

    private final TransactionExecutor executor;

    public DataBaseOperationExecutor(TransactionExecutor transactionExecutor) {
        requireNonNull(transactionExecutor, "constructor parameter must not be null ");
        executor = transactionExecutor;
    }

    public long executeStatement(String sqlQuery, List<Object> QueryParams) {
        checkArgs(sqlQuery, QueryParams);

        return executor.executeTransaction(connection -> {
            try (var preparedStatement =
                         connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

                for (var idx = 0; idx < QueryParams.size(); idx++) {
                    preparedStatement.setObject(idx + 1, QueryParams.get(idx));
                }
                preparedStatement.executeUpdate();

                try (var resultSet = preparedStatement.getGeneratedKeys()) {
                    resultSet.next();
                    return resultSet.getLong(PRIMARY_KEY_COLUMN_INDEX);
                }

            } catch (SQLException e) {
                throw new DataBaseOperationException("executeStatement error", e);
            }
        });
    }

    public <T> Optional<T> executeSelect(String sqlQuery, List<Object> QueryParams, Function<ResultSet, T> rsHandler) {
        checkArgs(sqlQuery, QueryParams);
        requireNonNull(rsHandler, "the ResultSet handler function must not be null ");

        return executor.executeTransaction(connection -> {
            try (var preparedStatement = connection.prepareStatement(sqlQuery)) {
                for (var idx = 0; idx < QueryParams.size(); idx++) {
                    preparedStatement.setObject(idx + 1, QueryParams.get(idx));
                }
                try (var resultSet = preparedStatement.executeQuery()) {
                    return Optional.ofNullable(rsHandler.apply(resultSet));
                }
            } catch (SQLException e) {
                throw new DataBaseOperationException("executeSelect error", e);
            }
        });
    }

    public boolean executeDelete(String sqlQuery, List<Object> params) {
        checkArgs(sqlQuery, params);

        return executor.executeTransaction(connection -> {
            try (var preparedStatement =
                         connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {

                for (var idx = 0; idx < params.size(); idx++) {
                    preparedStatement.setObject(idx + 1, params.get(idx));
                }

                return preparedStatement.executeUpdate() > 0;

            } catch (SQLException e) {
                throw new DataBaseOperationException("executeDelete error", e);
            }
        });
    }

    private void checkArgs(String sqlQuery, List<Object> params) {
        requireNonNull(sqlQuery, "sql-query must not be null ");
        requireNonNull(params, "the list with sql-query parameters can be empty, but not null ");
        if (sqlQuery.isEmpty()) {
            throw new IllegalArgumentException("sql query must not be empty ");
        }
    }
}
