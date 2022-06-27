package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.cache.MyCache;
import ru.otus.orm.datasource.DriverManagerDataSource;
import ru.otus.orm.entity.EntityClassMetaData;
import ru.otus.orm.entity.EntitySQLMetaData;
import ru.otus.orm.entity.implementation.DataTemplateJdbc;
import ru.otus.orm.entity.implementation.EntityClassMetaDataImpl;
import ru.otus.orm.entity.implementation.EntitySQLMetaDataImpl;
import ru.otus.orm.model.Client;
import ru.otus.orm.repository.DataTemplate;
import ru.otus.orm.repository.executor.DbExecutorImpl;
import ru.otus.orm.service.DBServiceClient;
import ru.otus.orm.service.DbServiceClientCacheImpl;
import ru.otus.orm.service.DbServiceClientImpl;
import ru.otus.orm.sessionmanager.TransactionRunner;
import ru.otus.orm.sessionmanager.TransactionRunnerJdbc;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

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

        testCache(transactionRunner, dataTemplateClient);
        log.info("---------------------------------------------------------------");
        runOperations(transactionRunner, dataTemplateClient, false);
        log.info("---------------------------------------------------------------");
        runOperations(transactionRunner, dataTemplateClient, true);

    }

    private static void runOperations(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplateClient,
                                      boolean useCache) {
        DBServiceClient dbServiceClient;
        if (useCache) {
            HwCache<String, Client> cache = new MyCache<>();
            dbServiceClient = new DbServiceClientCacheImpl(transactionRunner, dataTemplateClient, cache);
        } else {
            dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        }

        log.info("Use cache: " + useCache);

        long time = System.currentTimeMillis();
        var client = dbServiceClient.saveClient(new Client("dbServiceFirst"));
        log.info("save client in {} ms", System.currentTimeMillis() - time);

        time = System.currentTimeMillis();
        var clientSelected = dbServiceClient.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        log.info("clientSelected:{}, in {} ms", clientSelected, System.currentTimeMillis() - time);

        client.setName("dbServiceFirstUpdated");
        dbServiceClient.saveClient(client);

        time = System.currentTimeMillis();
        Client clientUpdatedSelected = dbServiceClient.getClient(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        log.info("clientUpdatedSelected:{}, in {} ms", clientUpdatedSelected, System.currentTimeMillis() - time);
    }

    private static void testCache(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplateClient) {
        HwCache<String, Client> cache = new MyCache<>();
        DBServiceClient dbServiceClient = new DbServiceClientCacheImpl(transactionRunner, dataTemplateClient, cache);

        List<Client> clients = new ArrayList<>();

        for (int i = 0; i < 1_000; i++) {
            clients.add(dbServiceClient.saveClient(new Client("client" + i)));
        }

        long time = System.currentTimeMillis();
        for (Client client : clients) {
            dbServiceClient.getClient(client.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        }
        long timeSelect = System.currentTimeMillis() - time;

        System.gc();

        time = System.currentTimeMillis();
        for (Client client : clients) {
            dbServiceClient.getClient(client.getId())
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + client.getId()));
        }
        long timeSelectAfterGc = System.currentTimeMillis() - time;

        log.info("Select from cache in {} ms", timeSelect);
        log.info("Select after gc in {} ms", timeSelectAfterGc);
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
