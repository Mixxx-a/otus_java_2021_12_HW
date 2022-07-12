package ru.otus.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.core.repository.AddressRepository;
import ru.otus.core.repository.ClientRepository;
import ru.otus.core.repository.PhoneRepository;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final PhoneRepository phoneRepository;
    private final TransactionManager transactionManager;

    public DbServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository,
                               AddressRepository addressRepository, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Client saveClient(String name, String addressStreet, List<String> phoneNumbers) {
        return transactionManager.doInTransaction(() -> {
            Client client = clientRepository.save(new Client(name));

            if (!"".equals(addressStreet)) {
                Address address = addressRepository.save(new Address(addressStreet, client.getId()));
                client.setAddress(address);
            }

            if (!phoneNumbers.isEmpty()) {
                Set<Phone> phones = new HashSet<>();
                for (String phoneNumber : phoneNumbers) {
                    if (!"".equals(phoneNumber)) {
                        Phone phone = phoneRepository.save(new Phone(phoneNumber, client.getId()));
                        phones.add(phone);
                    }
                }
                client.setPhones(phones);
            }

            log.info("saved client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientOptional = clientRepository.findById(id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(() -> {
            var clientList = clientRepository.findAll();
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
