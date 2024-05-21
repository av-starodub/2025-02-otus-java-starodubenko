package ru.otus.advjdbc.executors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class DataBaseOperationExecutor {

    private DataBaseOperationExecutor() {
    }

    public long executeStatement(Connection connection, String sqlQuery, List<Object> queryParams) {
        checkArgs(sqlQuery, queryParams);
        return -1;
    }

    public<T> Optional<T> executeSelect(
            Connection connection, String sqlQuery, List<Object> queryParams, Function<ResultSet, T> rsHandler) {
        checkArgs(sqlQuery, queryParams);
        requireNonNull(rsHandler, "the ResultSet handler function must not be null ");

        return Optional.empty();
    }

    public boolean executeDelete(Connection connection, String sqlQuery, List<Object> queryParams) {
        return false;
    }

    private void checkArgs(String sqlQuery, List<Object> queryParams) {
        requireNonNull(sqlQuery, "sql-query must not be null ");
        requireNonNull(queryParams, "the list with sql-query parameters can be empty, but not null ");
        if (sqlQuery.isEmpty()) {
            throw new IllegalArgumentException("sql query must not be empty ");
        }
    }
}
