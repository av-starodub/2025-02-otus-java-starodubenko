package ru.otus.orm.database.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.nio.file.Paths;

import static java.util.Objects.requireNonNull;

public final class DataSourceProvider {

    private DataSourceProvider() {
    }

    public static DataSource creatHikariConnectionPool(String fileName) {
        requireNonNull(fileName, "parameter fileName must not be null");
        if (!fileName.endsWith(".properties")) {
            throw new IllegalArgumentException("invalid file name: %s ".formatted(fileName));
        }

        var path = getHikariConfigPathPath(fileName);
        var config = new HikariConfig(path);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    private static String getHikariConfigPathPath(String fileName) {
        return Paths
                .get("HW07-jdbc_advanced","src", "main", "resources", fileName)
                .toAbsolutePath()
                .normalize()
                .toString();
    }
}
