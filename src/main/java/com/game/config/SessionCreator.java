package com.game.config;

import com.game.entity.Player;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@org.springframework.context.annotation.Configuration
@EnableWebMvc
@ComponentScan("com.game")
public class SessionCreator implements AutoCloseable {

    private final SessionFactory sessionFactory;

    @SneakyThrows
    public SessionCreator() {
        Configuration configuration = new Configuration();

        configuration.addAnnotatedClass(Player.class);

        sessionFactory = configuration.configure().buildSessionFactory();
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    public void close() {
        sessionFactory.close();
    }


}
