package ru.otus.orm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.orm.model.Client;
import ru.otus.orm.repository.DataTemplate;
import ru.otus.orm.sessionmanager.TransactionRunner;

import java.util.List;
import java.util.Optional;

public class DbServiceClientCacheImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Client> cache;

    public DbServiceClientCacheImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate,
                               HwCache<String, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created and cached client: {}", createdClient);
                cache.put(String.valueOf(createdClient.getId()), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            cache.put(String.valueOf(client.getId()), client);
            log.info("updated and cached client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(String.valueOf(id));
        if (cachedClient != null) {
            return Optional.of(cachedClient);
        } else {
            Optional<Client> optionalClient = transactionRunner.doInTransaction(connection -> {
                var clientOptional = dataTemplate.findById(connection, id);
                log.info("client: {}", clientOptional);
                return clientOptional;
            });
            optionalClient.ifPresent(client -> cache.put(String.valueOf(client.getId()), client));
            return optionalClient;
        }
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
