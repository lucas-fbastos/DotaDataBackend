package io.luck.AghsLabs.config;

import io.luck.AghsLabs.service.DbService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile({"prod","dev","local"})
public class ApplicationConfig {
    private final DbService dbService;
    private final String strategy;

    public ApplicationConfig(
            DbService dbService,
            @Value("${spring.jpa.hibernate.ddl-auto}") String strategy
    ) {
        this.dbService = dbService;
        this.strategy = strategy;
    }

    @Bean
    public boolean instantiateDatabase() throws ParseException {
        if (!"create".equals(strategy) && !"create-drop".equals(strategy))
            return false;

        dbService.instantiateTestDatabase();
        return true;
    }

}
