package ru.otus.servlet;

import ru.otus.crm.model.Client;
import ru.otus.crm.service.DBServiceClient;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class ClientsApiServlet extends HttpServlet {
    private static final String PARAM_NAME = "name";

    private final DBServiceClient dbServiceClient;

    public ClientsApiServlet(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter(PARAM_NAME);

        Client client = new Client(name);
        dbServiceClient.saveClient(client);

        response.getWriter().println("Created new client");

    }
}
