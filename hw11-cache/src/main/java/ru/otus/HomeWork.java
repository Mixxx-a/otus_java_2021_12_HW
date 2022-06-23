package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.orm.datasource.DriverManagerDataSource;
import ru.otus.orm.entity.EntityClassMetaData;
import ru.otus.orm.entity.EntitySQLMetaData;
import ru.otus.orm.entity.implementation.DataTemplateJdbc;
import ru.otus.orm.entity.implementation.EntityClassMetaDataImpl;
import ru.otus.orm.entity.implementation.EntitySQLMetaDataImpl;
import ru.otus.orm.model.Client;
import ru.otus.orm.repository.executor.DbExecutorImpl;
import ru.otus.orm.service.DbServiceClientImpl;
import ru.otus.orm.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient.getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        Client clientSecondUpdated = dbServiceClient.saveClient(new Client(2L, "dbServiceSecondUpdated"));
        Client clientSecondUpdatedSelected = dbServiceClient.getClient(clientSecondUpdated.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondUpdated.getId()));
        log.info("clientSecondUpdatedSelected:{}", clientSecondUpdatedSelected);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
