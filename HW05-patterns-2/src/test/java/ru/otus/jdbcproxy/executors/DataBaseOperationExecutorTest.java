package ru.otus.jdbcproxy.executors;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.jdbcproxy.exception.DataBaseOperationException;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@DisplayName("DataBaseOperationExecutorTest")
class DataBaseOperationExecutorTest {

    private static final Logger log = LoggerFactory.getLogger(DataBaseOperationExecutorTest.class);

    private static final String URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "";

    private static final String INSERT_QUERY = "INSERT INTO test(id, name) VALUES (?, ?)";
    private static final String UPDATE_QUERY = "UPDATE test SET name = ? WHERE id = ?";
    private static final String SELECT_QUERY = "SELECT name FROM test WHERE id = ?";
    private static final String DELETE_QUERY = "DELETE FROM test WHERE id = ?";

    private static final int RECORD_ID = 1;
    private static final String RECORD_NAME = "dummy";

    private static Flyway flyway;

    private final DataBaseOperationExecutor dbExecutor;

    private DataBaseOperationExecutorTest() {
        var dataSource = flyway.getConfiguration().getDataSource();
        var transactionExecutor = new TransactionExecutor(dataSource);
        dbExecutor = new DataBaseOperationExecutor(transactionExecutor);
    }

    @BeforeAll
    static void initDataBase() {
        var dataSource = createConnectionPool();
        flyway = configureFlyway(dataSource);
        log.info("data base migration started...");
        flyway.migrate();
        log.info("data base migration finished.");
        log.info("***");
    }

    @AfterAll
    static void deleteDataBase() {
        flyway.clean();
        log.info("data base deleted ");
    }

    @BeforeEach
    void setUp() {
        try {
            insertTestRecord(flyway.getConfiguration().getDataSource());
        } catch (SQLException e) {
            log.error("setUp exception %s".formatted(e.getMessage()));
        }
    }


    @AfterEach
    void tearDown() {
        try {
            clearDataBase(flyway.getConfiguration().getDataSource());
        } catch (SQLException e) {
            log.error("tearDown exception %s".formatted(e.getMessage()));
        }
    }


    @Test
    @DisplayName("checkInsertQueryExecution - should insert record and return new record id")
    void checkInsertQueryExecution() {
        var savedRecordId = dbExecutor.executeStatement(INSERT_QUERY, List.of(RECORD_ID + 1, RECORD_NAME));
        assertThat(savedRecordId).isEqualTo(2);
    }

    @Test
    @DisplayName("checkSelectQueryExecution - should execute select query correctly")
    void checkSelectQueryExecution() {
        var savedRecordName = dbExecutor.executeSelect(SELECT_QUERY, List.of(RECORD_ID), resultSet -> {
                    try {
                        if (resultSet.next()) {
                            return resultSet.getString("name");
                        }
                        return null;
                    } catch (SQLException e) {
                        throw new DataBaseOperationException("select error ", e);
                    }
                }
        ).orElse("");
        assertThat(savedRecordName).isEqualTo(RECORD_NAME);
    }

    @Test
    @DisplayName("checkUpdateQueryExecution - should return updated record id")
    void checkUpdateQueryExecution() {
        var updatedRecordId = dbExecutor.executeStatement(UPDATE_QUERY, List.of("updated", RECORD_ID));
        assertThat(updatedRecordId).isEqualTo(1);
    }

    @Test
    @DisplayName("checkDeleteQueryExecution - should execute delete query correctly")
    void checkDeleteQueryExecution() {
        var isRecordDeleted = dbExecutor.executeDelete(DELETE_QUERY, List.of(RECORD_ID));
        assertThat(isRecordDeleted).isTrue();
    }

    @Test
    @DisplayName("checkExceptionWrapping - should wrap any unexpected exception to DataBaseOperationException")
    void checkExceptionWrapping() {
        var thrown = catchThrowable(() ->
                dbExecutor.executeStatement(INSERT_QUERY, List.of(RECORD_ID, RECORD_NAME))
        );
        assertThat(thrown)
                .isInstanceOf(DataBaseOperationException.class)
                .hasMessageContaining("executeStatement error");
    }

    private static DataSource createConnectionPool() {
        var config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setConnectionTimeout(3000); //ms
        config.setIdleTimeout(60000); //ms
        config.setMaxLifetime(600000);//ms
        config.setAutoCommit(false);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setPoolName("HikariTestPool");
        config.setRegisterMbeans(true);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        config.setUsername(USER_NAME);
        config.setPassword(PASSWORD);

        return new HikariDataSource(config);
    }

    private static Flyway configureFlyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
    }

    private void insertTestRecord(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement(INSERT_QUERY)
        ) {
            var savePoint = connection.setSavepoint();
            try {
                pst.setObject(1, RECORD_ID);
                pst.setObject(2, RECORD_NAME);
                pst.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                log.error("addTestRecordToDataBase exception %s".formatted(e.getMessage()));
            }
        }
    }

    private void clearDataBase(DataSource dataSource) throws SQLException {
        try (var connection = dataSource.getConnection();
             var pst = connection.prepareStatement("DELETE FROM TEST")
        ) {
            var savePoint = connection.setSavepoint();
            try {
                pst.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback(savePoint);
                log.error("clearDataBase exception %s".formatted(e.getMessage()));
            }
        }
    }
}
