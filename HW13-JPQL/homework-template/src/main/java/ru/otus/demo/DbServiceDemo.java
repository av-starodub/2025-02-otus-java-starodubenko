package ru.otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        var migrationExecutorFlyway = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);

        try {
            migrationExecutorFlyway.executeMigrations();

            var sessionFactory = HibernateUtils.buildSessionFactory(
                    configuration, Client.class, Address.class, Phone.class);

            var transactionManager = new TransactionManagerHibernate(sessionFactory);

            var clientTemplate = new DataTemplateHibernate<>(Client.class);

            var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
            dbServiceClient.saveClient(new Client("dbServiceFirst"));

            var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
            var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
            log.info("clientSecondSelected:{}", clientSecondSelected);

            var addressForUpdate = new Address("street");
            var phonesForUpdate = List.of(new Phone("13-555-22"), new Phone("14-666-333"));
            var updatedClient = new Client(
                    clientSecondSelected.getId(), "dbServiceSecondUpdated", addressForUpdate, phonesForUpdate
            );

            dbServiceClient.saveClient(updatedClient);
            var clientUpdated = dbServiceClient.getClient(clientSecondSelected.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
            log.info("clientUpdated:{}", clientUpdated);

            log.info("All clients");
            dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            migrationExecutorFlyway.deleteDataBase();
        }
    }
}
