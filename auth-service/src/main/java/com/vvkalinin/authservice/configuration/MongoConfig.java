package com.vvkalinin.authservice.configuration;

import static org.springframework.data.domain.Sort.Direction.ASC;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;

@Configuration
public class MongoConfig {

    @Bean
    public CommandLineRunner initIndexes(MongoTemplate mongoTemplate) {
        return args -> {
            IndexOperations indexOps = mongoTemplate.indexOps("users"); // Collection name
            indexOps.ensureIndex(new Index().on("username", ASC).unique());
            indexOps.ensureIndex(new Index().on("email", ASC).unique());
        };
    }

}
