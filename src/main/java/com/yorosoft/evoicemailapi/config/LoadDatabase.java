package com.yorosoft.evoicemailapi.config;

import com.yorosoft.evoicemailapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserService userService) {
        return args -> {
            LOGGER.info("***************BEGIN ADMIN USER CREATION***************");
            userService.register("Ange Carmel","YORO", "codeur47");
            LOGGER.info("***************END ADMIN USER CREATION***************");
        };
    }
}
