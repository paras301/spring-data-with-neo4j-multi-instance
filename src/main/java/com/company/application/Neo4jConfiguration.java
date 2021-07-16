package com.company.application;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class Neo4jConfiguration {

    @Value("${spring.neo4j1.uri}")
    private String neo4j1_uri;

    @Value("${spring.neo4j1.authentication.username}")
    private String neo4j1_user;

    @Value("${spring.neo4j1.authentication.password}")
    private String neo4j1_password;
    
    @Value("${spring.neo4j1.database}")
    private String neo4j1_database;

    @Value("${spring.neo4j2.uri}")
    private String neo4j2_uri;

    @Value("${spring.neo4j2.authentication.username}")
    private String neo4j2_user;

    @Value("${spring.neo4j2.authentication.password}")
    private String neo4j2_password;
    
    @Value("${spring.neo4j2.database}")
    private String neo4j2_database;

    @Bean
    @Qualifier("neo4j1")
    public Driver neo4j1Driver() {
        return GraphDatabase.driver(neo4j1_uri, AuthTokens.basic(neo4j1_user, neo4j1_password));
    }

    @Bean
    @Qualifier("neo4j2")
    public Driver neo4j2Driver() {
        return GraphDatabase.driver(neo4j2_uri, AuthTokens.basic(neo4j2_user, neo4j2_password));
    }
}
