package com.company.vo;

import com.company.service.YamlPropertySourceFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Map;

@Configuration
@ConfigurationProperties
@PropertySource(value = "classpath:neo4jcypher/cypher.yaml", factory = YamlPropertySourceFactory.class)
@Getter
@RequiredArgsConstructor
public class CypherQueries {
    private final Map<String, String> cypher_queries;
}
