package ru.otus.advjdbc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.advjdbc.database.datasource.DataSourceProvider;
import ru.otus.advjdbc.database.dbexecutor.DataBaseOperationExecutor;
import ru.otus.advjdbc.database.dbmigration.DbMigrator;
import ru.otus.advjdbc.database.dbtransaction.TransactionExecutor;
import ru.otus.advjdbc.model.User;
import ru.otus.advjdbc.reposistory.AbstractRepository;
import ru.otus.advjdbc.service.AbstractRepositoryService;

public class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    // Домашнее задание:
    // - Реализовать класс DbMigrator - он должен при старте создавать все необходимые таблицы из файла init.sql
    // Доработать AbstractRepository
    // - Доделать findById(id), findAll(), update(), deleteById(id), deleteAll()
    // - Сделать возможность указывать имя столбца таблицы для конкретного поля (например, поле accountType маппить на столбец с именем account_type)
    // - Добавить проверки, если по какой-то причине невозможно проинициализировать репозиторий, необходимо бросать исключение, чтобы
    // программа завершила свою работу (в исключении надо объяснить что сломалось)
    // - Работу с полями объектов выполнять только через геттеры/сеттеры

    public static void main(String[] args) {

        var dataSource = DataSourceProvider.creatHikariConnectionPool("hikari.properties");
        var dbMigrator = new DbMigrator(dataSource);
        dbMigrator.migrate();

        try {
            var transactionExecutor = new TransactionExecutor(dataSource);
            var dbExecutor = new DataBaseOperationExecutor();

            var userRepository = new AbstractRepository<>(dbExecutor, User.class);
            var userRepositoryService = new AbstractRepositoryService<User>(userRepository, transactionExecutor);

            var user1 = new User("bob", "123", "bob");
            var user2 = new User("tom", "456", "tom");

            var savedUser1 = userRepositoryService.save(user1);
            LOG.info("saved user1 = {}", savedUser1);
            var savedUser2 = userRepositoryService.save(user2);
            LOG.info("saved user2 {}", savedUser2);

            var requiredUser1 = transactionExecutor.executeTransaction(connection -> {
                var requiredUserId = savedUser1.getId();
                return userRepository.findById(connection, requiredUserId).orElse(null);
            });
            LOG.info("required user1 = {}", requiredUser1);

            var user1ForUpdate = new User(savedUser1.getId(), "bob", "123", "updated_nickname");
            var updatedUser1 = userRepositoryService.save(user1ForUpdate);
            LOG.info("updated user1 {}", updatedUser1);

            var allSavedUsers = transactionExecutor.executeTransaction(userRepository::findAll);
            LOG.info("all users = {}", allSavedUsers);

            var isDeleted = transactionExecutor.executeTransaction(connection ->
                    userRepository.deleteById(connection, savedUser1.getId())
            );
            LOG.info("user1 deleted = {}", isDeleted);

            var isDeleteAll = transactionExecutor.executeTransaction(userRepository::deleteAll);
            LOG.info("all users deleted = {}", isDeleteAll);

            var allUsers = transactionExecutor.executeTransaction(userRepository::findAll);
            LOG.info("all users = {}", allUsers);

/*
            AbstractRepository<Account> accountAbstractRepository = new AbstractRepository<>(dataSource, Account.class);
            Account account = new Account(100L, "credit", "blocked");
            accountAbstractRepository.create(account);
*/
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            dbMigrator.deleteDataBase();
        }
    }
}
