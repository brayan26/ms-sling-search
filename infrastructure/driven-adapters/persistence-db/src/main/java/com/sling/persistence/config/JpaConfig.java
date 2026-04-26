package com.sling.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaConfig {

    @Bean
    public DBSecret dbSecret(Environment env) {
        return DBSecret.builder()
                .url(env.getProperty("adapters.persistence.datasource.url"))
                .username(env.getProperty("adapters.persistence.datasource.username"))
                .password(env.getProperty("adapters.persistence.datasource.password"))
                .build();
    }

    @Bean
    public DataSource datasource(DBSecret secret,
                                 @Value("${adapters.persistence.datasource.hikari.maximumPoolSize}") int maxPoolSize,
                                 @Value("${adapters.persistence.datasource.hikari.minimumIdle}") int minIdle,
                                 @Value("${adapters.persistence.datasource.hikari.idleTimeout}") long idleTimeout,
                                 @Value("${adapters.persistence.datasource.hikari.maxLifetime}") long maxLifetime,
                                 @Value("${adapters.persistence.datasource.hikari.connectionTimeout}") long connectionTimeout,
                                 @Value("${adapters.persistence.datasource.driverClassName}") String driverClass) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(secret.getUrl());
        config.setUsername(secret.getUsername());
        config.setPassword(secret.getPassword());
        config.setDriverClassName(driverClass);
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minIdle);
        config.setIdleTimeout(idleTimeout);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(connectionTimeout);
        return new HikariDataSource(config);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            @Value("${adapters.persistence.jpa.databasePlatform}") String dialect,
            @Value("${adapters.persistence.jpa.hibernate.ddl-auto}") String ddlAuto) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.sling.persistence");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", dialect);
        properties.setProperty("hibernate.hbm2ddl.auto", ddlAuto); // TODO: remove this for non auto create schema
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
