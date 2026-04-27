package com.sling.usecase.search;

import com.sling.model.search.port.SearchCountRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetSearchCountUseCaseTest {

    private static final String EXISTING_HASH = "abc123hash";
    private static final String NON_EXISTING_HASH = "nonExistentHash";
    private static final long EXPECTED_COUNT = 5L;
    private static final long ZERO_COUNT = 0L;

    @Mock
    private SearchCountRepositoryPort searchCountRepositoryPort;

    private GetSearchCountUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetSearchCountUseCase(searchCountRepositoryPort);
    }

    @Test
    @DisplayName("Should return the stored count when the hash exists in the repository")
    void shouldReturnStoredCountWhenHashExists() {
        when(searchCountRepositoryPort.getCount(EXISTING_HASH)).thenReturn(EXPECTED_COUNT);

        Long result = useCase.execute(EXISTING_HASH);

        assertEquals(EXPECTED_COUNT, result);
        verify(searchCountRepositoryPort).getCount(EXISTING_HASH);
    }

    @Test
    @DisplayName("Should return zero when the hash does not exist in the repository")
    void shouldReturnZeroWhenHashDoesNotExist() {
        when(searchCountRepositoryPort.getCount(NON_EXISTING_HASH)).thenReturn(ZERO_COUNT);

        Long result = useCase.execute(NON_EXISTING_HASH);

        assertEquals(ZERO_COUNT, result);
        verify(searchCountRepositoryPort).getCount(NON_EXISTING_HASH);
    }
}
