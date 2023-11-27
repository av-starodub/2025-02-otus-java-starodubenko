package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EntitySQLMetaDataImplTest {
    private EntitySQLMetaData entitySQLMetaDataClient;
    private EntitySQLMetaData entitySQLMetaDataManager;

    @BeforeEach
    void init() {
        var entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        entitySQLMetaDataClient = new EntitySQLMetaDataImpl<>(entityClassMetaDataClient);
        var entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        entitySQLMetaDataManager = new EntitySQLMetaDataImpl<>(entityClassMetaDataManager);

    }

    @Test
    @DisplayName("should create sql 'select by id' correctly")
    void getSelectByIdSqlTest() {
        var actualSelectByIdSql = entitySQLMetaDataClient.getSelectByIdSql();
        var expectedSelectByIdSql = "select id, name from client where id = ?";
        assertThat(actualSelectByIdSql).isEqualToIgnoringCase(expectedSelectByIdSql);
    }

    @Test
    @DisplayName("should create sql 'select all' correctly")
    void getSelectAllSqlTest() {
        var actualSelectByIdSql = entitySQLMetaDataClient.getSelectAllSql();
        var expectedSelectByIdSql = "select * from client";
        assertThat(actualSelectByIdSql).isEqualToIgnoringCase(expectedSelectByIdSql);
    }

    @Test
    @DisplayName("should create sql 'insert' correctly")
    void getInsertSqlTest() {
        var actualInsertSql = entitySQLMetaDataManager.getInsertSql();
        var expectedSelectByIdSql = "insert into manager(label, param1) values (?, ?)";
        assertThat(actualInsertSql).isEqualToIgnoringCase(expectedSelectByIdSql);
    }

    @Test
    @DisplayName("should create sql 'update' correctly")
    void getUpdateSqlTest() {
        var actualInsertSql = entitySQLMetaDataManager.getUpdateSql();
        var expectedSelectByIdSql = "update manager set label = ?, param1 = ? where no = ?";
        assertThat(actualInsertSql).isEqualToIgnoringCase(expectedSelectByIdSql);
    }
}
