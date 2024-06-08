package ru.otus.advjdbc.database.dbmigration;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

public class DbMigrator {
    private static final Logger LOG = LoggerFactory.getLogger(DbMigrator.class);

    private final Flyway flyway;

    public DbMigrator(DataSource dataSource) {
        flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();
    }

    public void migrate() {
        LOG.info("***");
        LOG.info("data base migration start ...");
        flyway.migrate();
        LOG.info("data base migration finished ");
        LOG.info("***");

    }

    public void deleteDataBase() {
        LOG.info("***");
        LOG.info("data base removing start ...");
        flyway.clean();
        LOG.info("data base removing finished ");
        LOG.info("***");
    }
}
