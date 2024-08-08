package otus;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.cfg.Configuration;
import otus.core.repository.DataTemplateHibernate;
import otus.core.repository.HibernateUtils;
import otus.core.sessionmanager.TransactionManagerHibernate;
import otus.crm.dbmigrations.MigrationsExecutorFlyway;
import otus.crm.model.Address;
import otus.crm.model.Client;
import otus.crm.model.Phone;
import otus.crm.service.DbServiceClientImpl;
import otus.crm.service.TemplateProcessor;
import otus.crm.service.TemplateProcessorImpl;
import otus.helpers.FileSystemHelper;
import otus.server.ClientsWebServer;
import otus.server.UsersWebServerWithFilterBasedSecurity;

import java.net.URI;

public class DbServiceDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        getMigrations(dbUrl, dbUserName, dbPassword);

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));

        LoginService loginService = new HashLoginService(REALM_NAME, configResource);

        ClientsWebServer usersWebServer =
                new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, loginService, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void getMigrations(String dbUrl, String dbUserName, String dbPassword) {
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }
}
