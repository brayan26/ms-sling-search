package com.sling.persistence;

import com.sling.persistence.entities.SearchCountEntity;
import com.sling.persistence.search.repositories.JpaSearchCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchCountRepositoryAdapterTest {

    private static final String EXISTING_HASH = "abc123hash";
    private static final String NON_EXISTING_HASH = "nonExistentHash";
    private static final long EXPECTED_COUNT = 5L;
    private static final long DEFAULT_COUNT = 0L;

    @Mock
    private JpaSearchCountRepository jpaSearchCountRepository;

    private SearchCountRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SearchCountRepositoryAdapter(jpaSearchCountRepository);
    }

    @Nested
    @DisplayName("getCount - Retrieves the search count for a given hash")
    class GetCountTests {

        @Test
        @DisplayName("Should return the stored count when the hash exists in the database")
        void shouldReturnStoredCountWhenHashExists() {
            SearchCountEntity entity = new SearchCountEntity(EXISTING_HASH, EXPECTED_COUNT);
            when(jpaSearchCountRepository.findById(EXISTING_HASH)).thenReturn(Optional.of(entity));

            long result = adapter.getCount(EXISTING_HASH);

            assertEquals(EXPECTED_COUNT, result);
            verify(jpaSearchCountRepository).findById(EXISTING_HASH);
        }

        @Test
        @DisplayName("Should return zero when the hash does not exist in the database")
        void shouldReturnZeroWhenHashDoesNotExist() {
            when(jpaSearchCountRepository.findById(NON_EXISTING_HASH)).thenReturn(Optional.empty());

            long result = adapter.getCount(NON_EXISTING_HASH);

            assertEquals(DEFAULT_COUNT, result);
            verify(jpaSearchCountRepository).findById(NON_EXISTING_HASH);
        }
    }

    @Nested
    @DisplayName("increment - Increments the search count for a given hash")
    class IncrementTests {

        @Test
        @DisplayName("Should delegate the increment operation to the JPA repository")
        void shouldDelegateIncrementToJpaRepository() {
            adapter.increment(EXISTING_HASH);

            verify(jpaSearchCountRepository).increment(EXISTING_HASH);
        }
    }
}
