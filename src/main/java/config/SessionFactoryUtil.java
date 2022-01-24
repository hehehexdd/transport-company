package config;

import entity.*;
import entity.TransportCompany;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration();
            configuration.addAnnotatedClass(TransportCompany.class);
            configuration.addAnnotatedClass(Driver.class);
            configuration.addAnnotatedClass(Vehicle.class);
            configuration.addAnnotatedClass(Logistics.class);
            configuration.addAnnotatedClass(Client.class);
            configuration.addAnnotatedClass(Qualification.class);
            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}
