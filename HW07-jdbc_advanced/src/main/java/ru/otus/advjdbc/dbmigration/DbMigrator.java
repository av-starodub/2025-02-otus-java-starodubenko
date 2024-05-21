package ru.otus.advjdbc.dbmigration;

import ru.otus.advjdbc.DataSource;

public class DbMigrator {
    private DataSource dataSource;

    public DbMigrator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void migrate() {

    }
}
