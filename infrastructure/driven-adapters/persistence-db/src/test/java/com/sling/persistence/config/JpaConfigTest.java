package com.sling.persistence.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaConfigTest {

    private static final String URL_PROPERTY = "adapters.persistence.datasource.url";
    private static final String USERNAME_PROPERTY = "adapters.persistence.datasource.username";
    private static final String PASSWORD_PROPERTY = "adapters.persistence.datasource.password";

    private static final String EXPECTED_URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String EXPECTED_USERNAME = "admin";
    private static final String EXPECTED_PASSWORD = "secret123";

    @Mock
    private Environment environment;

    @Test
    @DisplayName("Should create DBSecret bean with properties resolved from the environment")
    void shouldCreateDBSecretBeanFromEnvironment() {
        when(environment.getProperty(URL_PROPERTY)).thenReturn(EXPECTED_URL);
        when(environment.getProperty(USERNAME_PROPERTY)).thenReturn(EXPECTED_USERNAME);
        when(environment.getProperty(PASSWORD_PROPERTY)).thenReturn(EXPECTED_PASSWORD);

        JpaConfig jpaConfig = new JpaConfig();
        DBSecret secret = jpaConfig.dbSecret(environment);

        assertNotNull(secret);
        assertEquals(EXPECTED_URL, secret.getUrl());
        assertEquals(EXPECTED_USERNAME, secret.getUsername());
        assertEquals(EXPECTED_PASSWORD, secret.getPassword());
    }
}
