package ru.otus.servlet;

import com.google.gson.Gson;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class ClientsApiServlet extends HttpServlet {
    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONE = "phone";
    private static final String EMPTY_STRING = "";

    private final DBServiceClient dbServiceClient;
    private final Gson gson;

    public ClientsApiServlet(DBServiceClient dbServiceClient, Gson gson) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<?, ?> properties = gson.fromJson(request.getReader(), Map.class);

        String nameString = (String) properties.get(PARAM_NAME);
        if (nameString != null && !EMPTY_STRING.equals(nameString)) {
            Client client = new Client(nameString);

            String addressString = (String) properties.get(PARAM_ADDRESS);
            if (addressString != null && !EMPTY_STRING.equals(addressString)) {
                Address address = new Address();
                address.setStreet(addressString);
                client.setAddress(address);
            }

            String phoneString = (String) properties.get(PARAM_PHONE);
            if (phoneString != null && !EMPTY_STRING.equals(phoneString)) {
                Phone phone = new Phone();
                phone.setNumber(phoneString);
                client.addPhone(phone);
            }
            Client savedClient = dbServiceClient.saveClient(client);

            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(savedClient));
            response.setStatus(201);
        }
    }
}
