package ru.otus.orm.database.dbtransaction;

import ru.otus.orm.exceptions.DataBaseOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.function.Function;

public final class TransactionExecutor {
    private final DataSource dataSource;

    public TransactionExecutor(DataSource ds) {
        this.dataSource = ds;
    }

    public <T> T executeTransaction(Function<Connection, T> action) {
        return wrapException(() -> {
            try (var connection = dataSource.getConnection()) {
                var savePoint = connection.setSavepoint();
                try {
                    var result = action.apply(connection);
                    connection.commit();
                    return result;
                } catch (SQLException e) {
                    connection.rollback(savePoint);
                    throw new DataBaseOperationException("transaction exception", e);
                }
            }
        });
    }

    public <T> T wrapException(Callable<T> action) {
        try {
            return action.call();
        } catch (Exception e) {
            throw new DataBaseOperationException(e.getMessage(), e);
        }
    }
}
