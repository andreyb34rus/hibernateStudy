package com.sample.secondLevelCache;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;

@Slf4j
public class SLChTest {
    //private static final Logger log = LoggerFactory.getLogger(HibernateRunner.class); // provides @Slf4j

    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(Item.class);
        configuration.addAnnotatedClass(Cart.class);
        configuration.configure();

        try {
            SessionFactory sessionFactory = configuration.buildSessionFactory();
            Item item1 = Item.builder()
                    .name("Philips 500X")
                    .price(8499F)
                    .build();
            Item item2 = Item.builder()
                    .name("Sony 33JB")
                    .price(74500F)
                    .build();
            Cart cart = Cart.builder()
                    .owner("John")
                    .build();
            cart.addItem(item1);
            cart.addItem(item2);

            try (Session session1 = sessionFactory.openSession()) {
                session1.beginTransaction();
                session1.saveOrUpdate(cart);
                session1.clear();
                item1 = session1.find(Item.class, 1L);
                session1.getTransaction().commit();
            }
            try (Session session2 = sessionFactory.openSession()) {
                session2.beginTransaction();
                item2 = session2.find(Item.class, 1L); // здесь не должно быть запросов в БД
                session2.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
