package ru.otus.core.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findById(long id);

    List<Client> findAll();

    Client save(Client client);
}
