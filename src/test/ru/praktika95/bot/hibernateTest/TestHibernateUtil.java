package ru.praktika95.bot.hibernateTest;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import ru.praktika95.bot.hibernate.DataBaseSettings;

import java.util.HashMap;
import java.util.Map;

public class TestHibernateUtil {
    private static StandardServiceRegistry registry;
    private static volatile SessionFactory sessionFactory;

    private TestHibernateUtil() {

    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (TestHibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
                        Map<String, String> settings = new HashMap<>();
                        settings.put(Environment.DRIVER, DataBaseSettings.getDriver());
                        settings.put(Environment.URL, DataBaseSettings.getUrl());
                        settings.put(Environment.USER, DataBaseSettings.getUser());
                        settings.put(Environment.PASS, DataBaseSettings.getPass());
                        settings.put(Environment.DIALECT, DataBaseSettings.getDialect());

                        registryBuilder.applySettings(settings);
                        registry = registryBuilder.build();
                        MetadataSources sources = new MetadataSources(registry);
                        sources.addAnnotatedClass(TestUser.class);
                        Metadata metadata = sources.getMetadataBuilder().build();
                        sessionFactory = metadata.getSessionFactoryBuilder().build();

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (registry != null) {
                            StandardServiceRegistryBuilder.destroy(registry);
                        }
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void close() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }
}
