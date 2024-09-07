package ru.otus.app;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.app.controller.ConsoleController;
import ru.otus.app.dao.ClientDao;
import ru.otus.app.dao.HibernateUtils;
import ru.otus.app.dao.ProductDao;
import ru.otus.app.dao.PurchaseDao;
import ru.otus.app.db.init.DatabaseInitializer;
import ru.otus.app.db.loader.DataLoader;
import ru.otus.app.db.loader.DataProperties;
import ru.otus.app.db.migration.MigrationsExecutorFlyway;
import ru.otus.app.db.sessionmanager.TransactionManager;
import ru.otus.app.dto.ClientDto;
import ru.otus.app.dto.ProductDto;
import ru.otus.app.dto.PurchaseDto;
import ru.otus.app.model.Client;
import ru.otus.app.model.Product;
import ru.otus.app.model.Purchase;
import ru.otus.app.service.ClientService;
import ru.otus.app.service.ProductService;
import ru.otus.app.service.PurchaseService;

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

            var sessionFactory = HibernateUtils.buildSessionFactory(
                    configuration, Product.class, Client.class, Purchase.class
            );

            var transactionManager = new TransactionManager(sessionFactory);
            var dbInitializer = new DatabaseInitializer(transactionManager);

            var productProperties = DataProperties.create("data-products.yml");
            var products = DataLoader.load(productProperties, ProductDto.class);
            dbInitializer.init(products);

            var clientProperties = DataProperties.create("data-clients.yml");
            var clients = DataLoader.load(clientProperties, ClientDto.class);
            dbInitializer.init(clients);

            var purchaseProperties = DataProperties.create("data-purchases.yml");
            var purchase = DataLoader.load(purchaseProperties, PurchaseDto.class);
            System.out.println(purchase);
            dbInitializer.init(purchase);

            var purchaseDao = new PurchaseDao();
            var productDao = new ProductDao();
            var clientDao = new ClientDao();

            var productService = new ProductService(productDao, purchaseDao, transactionManager);
            var clientService = new ClientService(clientDao, purchaseDao, transactionManager);
            var purchaseService = new PurchaseService(purchaseDao, transactionManager);

            var consoleController = new ConsoleController(productService, clientService, purchaseService);
            consoleController.run();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            migrationExecutorFlyway.deleteDataBase();
        }
    }
}
