package otus.crm.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import otus.crm.model.Address;
import otus.crm.model.Client;
import otus.crm.model.Phone;
import otus.crm.service.DBServiceClient;
import otus.crm.service.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientsServlet extends HttpServlet {
    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ATTR_RANDOM_CLIENT = "client";
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        List<Client> all = dbServiceClient.findAll();
        paramsMap.put(TEMPLATE_ATTR_RANDOM_CLIENT, all);

        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response) throws IOException {
        String name = req.getParameter("name");
        String street = req.getParameter("street");
        String phones = req.getParameter("phones");

        List<Phone> phoneList = Arrays.stream(phones.split(","))
                .map(phone -> new Phone())
                .toList();

        Client client = dbServiceClient.saveClient(new Client(name, new Address(street), phoneList));
        if (client != null) {
            response.setStatus(200);
            response.sendRedirect("/clients");
        } else {
            response.sendError(500, "Не удалось добавить клиента");
        }
    }
}
