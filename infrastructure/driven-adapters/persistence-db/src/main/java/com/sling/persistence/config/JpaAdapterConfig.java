package com.sling.persistence.config;

import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.sling.persistence.search.repositories")
@EntityScan(basePackages = "com.sling.persistence.entities")
public class JpaAdapterConfig {
}
