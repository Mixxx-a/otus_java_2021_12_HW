package ru.otus.service;


import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DBServiceClient {

    Client saveClient(String name, String addressStreet, Set<String> phoneNumbers);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
