package ru.otus.servlet;

import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientsServlet extends HttpServlet {

    private static final String TEMPLATE_PATH = "clients.html";
    private static final String TEMPLATE_ATTR_CLIENTS = "clients";
    private final TemplateProcessor templateProcessor;
    private final DBServiceClient dbServiceClient;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(TEMPLATE_ATTR_CLIENTS, dbServiceClient.findAll());

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(TEMPLATE_PATH, params));
    }

}