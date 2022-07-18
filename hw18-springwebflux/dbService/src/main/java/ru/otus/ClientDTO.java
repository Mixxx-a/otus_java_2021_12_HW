package ru.otus;

import ru.otus.crm.model.Client;

import java.util.Set;

public class ClientDTO {
    private Long id;
    private String name;
    private String address;
    private Set<String> phones;

    public ClientDTO() {
    }

    public ClientDTO(Client client) {
        this(client.getId(), client.getName(), client.getStreet(), client.getPhonesStrings());
    }

    public ClientDTO(Long id, String name, String address, Set<String> phones) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<String> getPhones() {
        return phones;
    }

    public void setPhones(Set<String> phones) {
        this.phones = phones;
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
