package ru.otus.app;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.dbmigation.MigrationsExecutorFlyway;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        var migrationExecutorFlyway = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);

        try {
            migrationExecutorFlyway.executeMigrations();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            migrationExecutorFlyway.deleteDataBase();
        }
    }
}
