package ru.otus.app.db.migration;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MigrationsExecutorFlyway {
    private static final Logger logger = LoggerFactory.getLogger(MigrationsExecutorFlyway.class);

    private final Flyway flyway;

    public MigrationsExecutorFlyway(String dbUrl, String dbUserName, String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/db/migration")
                .cleanDisabled(false)
                .load();
    }

    public void executeMigrations() {
        logger.info("db migration started...");
        flyway.migrate();
        logger.info("db migration finished.");
    }

    public void deleteDataBase() {
        logger.info("db cleaning started...");
        flyway.clean();
        logger.info("db cleaning finished.");
    }
}
