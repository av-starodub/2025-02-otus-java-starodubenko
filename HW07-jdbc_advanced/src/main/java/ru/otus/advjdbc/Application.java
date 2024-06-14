package ru.otus.advjdbc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.advjdbc.database.datasource.DataSourceProvider;
import ru.otus.advjdbc.database.dbexecutor.DataBaseOperationExecutor;
import ru.otus.advjdbc.database.dbmigration.DbMigrator;
import ru.otus.advjdbc.database.dbtransaction.TransactionExecutor;
import ru.otus.advjdbc.entity.EntityMapper;
import ru.otus.advjdbc.entity.EntityMetaData;
import ru.otus.advjdbc.model.Account;
import ru.otus.advjdbc.model.User;
import ru.otus.advjdbc.reposistory.AbstractRepository;
import ru.otus.advjdbc.service.AbstractRepositoryService;

public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {

        var dataSource = DataSourceProvider.creatHikariConnectionPool("hikari.properties");
        var dbMigrator = new DbMigrator(dataSource);
        dbMigrator.migrate();

        try {
            LOG.info("\nORM demo start ...");
            var transactionExecutor = new TransactionExecutor(dataSource);
            var dbExecutor = new DataBaseOperationExecutor();

            var userMetaData = new EntityMetaData<>(User.class);
            var userMapper = new EntityMapper<>(userMetaData);

            var userRepository = new AbstractRepository<>(dbExecutor, userMapper);
            var userRepositoryService = new AbstractRepositoryService<>(userRepository, transactionExecutor);

            var user1 = new User("bob", "123", "bob");
            var user2 = new User("tom", "456", "tom");

            var savedUser1 = userRepositoryService.save(user1);
            LOG.info("saved user1: {}", savedUser1);
            var savedUser2 = userRepositoryService.save(user2);
            LOG.info("saved user2: {}", savedUser2);

            var savedUser1Id = savedUser1.getId();
            var requiredUser1 = userRepositoryService.get(savedUser1Id)
                    .orElseThrow(() -> new RuntimeException("user with id=%d not found".formatted(savedUser1Id)));
            LOG.info("required user1: {}", requiredUser1);

            var user1ForUpdate = new User(savedUser1Id, "bob", "123", "updated_nickname");
            var updatedUser1 = userRepositoryService.save(user1ForUpdate);
            LOG.info("updated user1: {}", updatedUser1);

            var allSavedUsers = userRepositoryService.getAll();
            LOG.info("all users: {}", allSavedUsers);

            var isUser1Deleted = userRepositoryService.remove(savedUser1Id);
            LOG.info("user1 deleted: {}", isUser1Deleted);

            var isAllDeleted = userRepositoryService.removeAll();
            LOG.info("all users deleted: {}", isAllDeleted);

            var allUsers = userRepositoryService.getAll();
            LOG.info("all users: {}", allUsers);

            var accountMetaData = new EntityMetaData<>(Account.class);
            var accountMapper = new EntityMapper<>(accountMetaData);

            var accountRepository = new AbstractRepository<>(dbExecutor, accountMapper);
            var accountRepositoryService = new AbstractRepositoryService<>(accountRepository, transactionExecutor);

            var account = new Account(100L, "credit", "blocked");

            var savedAccount = accountRepositoryService.save(account);
            LOG.info("saved account: {}", savedAccount);

            var savedAccountId = savedAccount.getId();

            var requiredAccount = accountRepositoryService.get(savedAccountId)
                    .orElseThrow(() -> new RuntimeException("account with id=%d not found".formatted(savedAccountId)));
            ;
            LOG.info("required account: {}", requiredAccount);

            requiredAccount.setAmount(500L);
            var updatedAccount = accountRepositoryService.save(requiredAccount);
            LOG.info("updated account: {}", updatedAccount);

            var isAccountDeleted = accountRepositoryService.remove(savedAccountId);
            LOG.info("account with id={} deleted: {}", savedAccountId, isAccountDeleted);

            var allAccounts = accountRepositoryService.getAll();
            LOG.info("all accounts: {}", allAccounts);

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            LOG.info("\nORM demo finished");
            dbMigrator.deleteDataBase();
        }
    }
}
