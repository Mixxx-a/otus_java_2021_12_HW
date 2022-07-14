package ru.otus.crm.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.stream.Collectors;

@Table("client")
public class Client {

    @Id
    private Long id;
    private String name;
    @MappedCollection(idColumn = "client_id")
    private Address address;
    @MappedCollection(idColumn = "client_id")
    private Set<Phone> phones;

    public Client() {
    }

    public Client(String name) {
        this(null, name, null, null);
    }

    @PersistenceCreator
    public Client(Long id, String name, Address address, Set<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<Phone> getPhones() {
        return phones;
    }

    public void setPhones(Set<Phone> phones) {
        this.phones = phones;
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    public void removePhone(Phone phone) {
        phones.remove(phone);
    }

    public String getStreet() {
        if (this.getAddress() != null) {
            return this.getAddress().getStreet();
        } else return null;
    }

    public Set<String> getPhonesStrings() {
        if (this.getPhones() != null) {
            return this.getPhones().stream().map(Phone::getNumber).collect(Collectors.toSet());
        } else return null;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("Client{id=")
                .append(id)
                .append(", name='")
                .append(name)
                .append("'");
        if (address != null) {
            stringBuilder.append(", address='")
                    .append(address)
                    .append("'");
        }
        if (phones != null) {
            stringBuilder.append(", phones='")
                    .append(phones)
                    .append("'");
        }
        return stringBuilder.append("}").toString();
    }
}
