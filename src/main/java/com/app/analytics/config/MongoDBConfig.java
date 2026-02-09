package com.app.analytics.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Source - https://stackoverflow.com/a/77213123
// Posted by summerisbetterthanwinter
// Retrieved 2026-02-08, License - CC BY-SA 4.0
@Configuration
public class MongoDBConfig {
    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb+srv://swayamscse22_db_user:dfXdKFKKHQstcIW9@cluster0.gisau4i.mongodb.net/?appName=Cluster0");
    }
}