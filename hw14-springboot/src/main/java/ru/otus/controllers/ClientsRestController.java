package ru.otus.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.crm.model.Client;
import ru.otus.service.DBServiceClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ClientsRestController {

    private final DBServiceClient dbServiceClient;

    public ClientsRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @PostMapping("/client/save")
    public Client clientSave(@RequestBody Map<String, String> params) {
        List<String> phones = new ArrayList<>();
        phones.add(params.get("phone"));
        return dbServiceClient.saveClient(params.get("name"), params.get("address"), phones);
    }

}