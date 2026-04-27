package com.sling.persistence.entities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SearchCountEntityTest {

    private static final String EXPECTED_HASH = "abc123hash";
    private static final long EXPECTED_COUNT = 10L;

    @Test
    @DisplayName("Should create entity with all args constructor and return correct values via getters")
    void shouldCreateEntityWithAllArgsAndReturnCorrectValues() {
        SearchCountEntity entity = new SearchCountEntity(EXPECTED_HASH, EXPECTED_COUNT);

        assertEquals(EXPECTED_HASH, entity.getHash());
        assertEquals(EXPECTED_COUNT, entity.getCount());
    }

    @Test
    @DisplayName("Should create entity with no args constructor and set values via setters")
    void shouldCreateEntityWithNoArgsAndSetValues() {
        SearchCountEntity entity = new SearchCountEntity();
        entity.setHash(EXPECTED_HASH);
        entity.setCount(EXPECTED_COUNT);

        assertEquals(EXPECTED_HASH, entity.getHash());
        assertEquals(EXPECTED_COUNT, entity.getCount());
    }

    @Test
    @DisplayName("Should create entity with default values using no args constructor")
    void shouldCreateEntityWithDefaultValues() {
        SearchCountEntity entity = new SearchCountEntity();

        assertNotNull(entity);
        assertEquals(0L, entity.getCount());
    }
}
