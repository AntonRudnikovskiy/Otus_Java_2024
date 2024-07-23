package otus.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import otus.cachehw.HwCache;
import otus.cachehw.MyCache;
import otus.core.repository.DataTemplateHibernate;
import otus.core.repository.HibernateUtils;
import otus.core.sessionmanager.TransactionManagerHibernate;
import otus.crm.dbmigrations.MigrationsExecutorFlyway;
import otus.crm.model.Address;
import otus.crm.model.Client;
import otus.crm.model.Phone;
import otus.crm.service.DbServiceClientImpl;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class,
                Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        // клиенты из базы
        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected1 = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected1);

        var clientSecondSelected2 = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected2);

        var clientSecondSelected3 = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected3);

        var clientSecondSelected4 = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected4);

        var clientSecondSelected5 = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected5);


        log.info("All clients");
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));

        // клиенты из кеша
        HwCache<String, Client> cache = new MyCache<>();
        cache.put("client", new Client(clientSecondSelected1.getId(), "dbServiceSecondUpdated"));
        cache.put("client", new Client(clientSecondSelected2.getId(), "dbServiceSecondUpdated"));
        cache.put("client", new Client(clientSecondSelected3.getId(), "dbServiceSecondUpdated"));
        cache.put("client", new Client(clientSecondSelected4.getId(), "dbServiceSecondUpdated"));
        cache.put("client", new Client(clientSecondSelected5.getId(), "dbServiceSecondUpdated"));

        for (int i = 0; i < 4; i++) {
            log.info("clients from cache");
            cache.get("client");
        }
    }
}
