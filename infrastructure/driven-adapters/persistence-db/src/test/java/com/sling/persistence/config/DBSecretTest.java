package com.sling.persistence.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DBSecretTest {

    private static final String EXPECTED_URL = "jdbc:postgresql://localhost:5432/testdb";
    private static final String EXPECTED_USERNAME = "admin";
    private static final String EXPECTED_PASSWORD = "secret123";

    @Test
    @DisplayName("Should build DBSecret with all properties correctly assigned")
    void shouldBuildDBSecretWithAllProperties() {
        DBSecret secret = DBSecret.builder()
                .url(EXPECTED_URL)
                .username(EXPECTED_USERNAME)
                .password(EXPECTED_PASSWORD)
                .build();

        assertNotNull(secret);
        assertEquals(EXPECTED_URL, secret.getUrl());
        assertEquals(EXPECTED_USERNAME, secret.getUsername());
        assertEquals(EXPECTED_PASSWORD, secret.getPassword());
    }
}
