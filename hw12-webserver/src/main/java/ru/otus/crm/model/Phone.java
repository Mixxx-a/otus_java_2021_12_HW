package ru.otus.crm.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phone_generator")
    @SequenceGenerator(name = "phone_generator", sequenceName = "phone_sequence", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    @Expose
    private String number;

    @ManyToOne(targetEntity = Client.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public Phone() {
    }

    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
