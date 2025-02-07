package com.game.config;

import liquibase.Scope;
import liquibase.command.CommandScope;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class LiquibaseConfig {
    @PostConstruct
    public void init() {
        try {
            Scope.child(Scope.Attr.resourceAccessor, new ClassLoaderResourceAccessor(), () -> {
                CommandScope update = new CommandScope("update");

                update.addArgumentValue("changelogFile", "migrations/db.changelog.xml");
                update.addArgumentValue("url", "jdbc:postgresql://localhost:5432/game");
                update.addArgumentValue("username", "postgres");
                update.addArgumentValue("password", "postgres");
                update.execute();
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }    }
}
