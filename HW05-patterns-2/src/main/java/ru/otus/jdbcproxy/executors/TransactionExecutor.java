package ru.otus.jdbcproxy.executors;

import ru.otus.jdbcproxy.exception.DataBaseOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public final class TransactionExecutor {

    private final DataSource dataSource;

    public TransactionExecutor(DataSource ds) {
        requireNonNull(ds, "constructor parameter must not be null ");
        dataSource = ds;
    }

    public <T> T executeTransaction(Function<Connection, T> action) {
        requireNonNull(action, "action must not be null ");

        return wrapException(() -> {
            try (var connection = dataSource.getConnection()) {
                var savePoint = connection.setSavepoint();
                try {
                    var result = action.apply(connection);
                    connection.commit();
                    return result;
                } catch (SQLException e) {
                    connection.rollback(savePoint);
                    throw new DataBaseOperationException("transaction error ", e);
                }
            }
        });
    }

    private <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }
}
