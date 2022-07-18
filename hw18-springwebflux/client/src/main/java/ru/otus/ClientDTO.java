package ru.otus;

import java.util.HashSet;
import java.util.Set;

public class ClientDTO {
    private Long id;
    private String name;
    private String address;
    private Set<String> phones = new HashSet<>();

    public ClientDTO() {
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

    public void setPhone(String phone) {
        this.phones.add(phone);
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

