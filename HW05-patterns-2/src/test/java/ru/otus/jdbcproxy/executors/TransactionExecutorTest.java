package ru.otus.jdbcproxy.executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.jdbcproxy.exception.DataBaseOperationException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransactionExecutorTest")
class TransactionExecutorTest {
    @Mock
    private Function<Connection, ?> acton;
    @Mock
    private Connection connection;
    @Mock
    private DataSource dataSource;
    @InjectMocks
    private TransactionExecutor transactionExecutor;

    @BeforeEach
    void setUp() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    @DisplayName("checkWrappingSqlException - should wrap SqlException to DataBaseOperationException")
    void checkSqlExceptionWrapping() {
        when(acton.apply(connection)).thenAnswer(ans -> {
            throw new SQLException();
        });

        var thrown = catchThrowable(() -> transactionExecutor.executeTransaction(acton));

        assertThat(thrown)
                .isInstanceOf(DataBaseOperationException.class)
                .hasRootCauseInstanceOf(SQLException.class)
                .hasMessageContaining("transaction error");
    }

    @Test
    @DisplayName("checkUnexpectedExceptionWrapping - should wrap any unexpected exception to DataBaseOperationException")
    void checkUnexpectedExceptionWrapping() {
        when(acton.apply(connection)).thenThrow(new RuntimeException("unexpected exception"));

        var thrown = catchThrowable(() -> transactionExecutor.executeTransaction(acton));

        assertThat(thrown)
                .isInstanceOf(DataBaseOperationException.class)
                .hasRootCauseInstanceOf(RuntimeException.class)
                .hasMessageContaining("unexpected exception");
    }
}
