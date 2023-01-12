package com.br.app.DotaTrainerBackend.config;

import com.br.app.DotaTrainerBackend.service.DbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.ParseException;

@Configuration
@Profile({"prod","dev"})
public class ApplicationConfig {
    @Autowired
    private DbService dbService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String strategy;

    @Bean
    public boolean instantiateDatabase() throws ParseException {
       // if (!"create".equals(strategy) && !"create-drop".equals(strategy))
         //   return false;

        dbService.instantiateTestDatabase();
        return true;
    }

}
