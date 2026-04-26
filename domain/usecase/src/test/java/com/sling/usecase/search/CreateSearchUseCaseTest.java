package com.sling.usecase.search;

import com.sling.model.search.Search;
import com.sling.model.search.service.SearchHashServicePort;
import com.sling.model.search.valueobject.SearchId;
import com.sling.model.search.port.EventPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateSearchUseCaseTest {

    private static final String HOTEL_ID = "1234";
    private static final String CHECK_IN = LocalDate.of(2026, 5, 1).toString();
    private static final String CHECK_OUT = LocalDate.of(2026, 5, 5).toString();
    private static final List<Integer> AGES = List.of(30, 25);
    private static final String GENERATED_HASH = "abc123hash";
    private static final String DIFFERENT_HASH = "xyz789hash";
    private static final String ERROR_MESSAGE = "SearchId cannot be null or blank";

    @Mock
    private EventPublisherPort publisherPort;

    @Mock
    private SearchHashServicePort searchHashServicePort;

    private CreateSearchUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateSearchUseCase(publisherPort, searchHashServicePort);
    }

    @Test
    @DisplayName("Should generate hash, publish search with hash and return SearchId")
    void shouldGenerateHashPublishSearchAndReturnSearchId() {
        Search command = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(AGES)
                .build();

        when(searchHashServicePort.generateHash(command)).thenReturn(GENERATED_HASH);

        SearchId result = useCase.execute(command);

        assertEquals(GENERATED_HASH, result.value());

        verify(searchHashServicePort).generateHash(command);

        ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
        verify(publisherPort).publish(captor.capture());

        Search published = captor.getValue();
        assertEquals(HOTEL_ID, published.getHotelId());
        assertEquals(CHECK_IN, published.getCheckIn());
        assertEquals(CHECK_OUT, published.getCheckOut());
        assertEquals(AGES, published.getAges());
        assertEquals(GENERATED_HASH, published.getHash());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when generated hash is null")
    void shouldThrowExceptionWhenGeneratedHashIsNull() {
        Search command = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(AGES)
                .build();

        when(searchHashServicePort.generateHash(command)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> useCase.execute(command));

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when generated hash is blank")
    void shouldThrowExceptionWhenGeneratedHashIsBlank() {
        Search command = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(AGES)
                .build();

        when(searchHashServicePort.generateHash(command)).thenReturn("   ");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                                                          () -> useCase.execute(command));

        assertEquals(ERROR_MESSAGE, exception.getMessage());
    }

    @Test
    @DisplayName("Should return the same hash when use case is invoked twice with the same input")
    void shouldReturnSameHashWhenInvokedTwiceWithSameInput() {
        Search command = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(AGES)
                .build();

        when(searchHashServicePort.generateHash(command)).thenReturn(GENERATED_HASH);

        SearchId firstResult = useCase.execute(command);
        SearchId secondResult = useCase.execute(command);

        assertEquals(firstResult.value(), secondResult.value());
        verify(searchHashServicePort, times(2)).generateHash(command);
    }

    @Test
    @DisplayName("Should return different hash when ages order changes")
    void shouldReturnDifferentHashWhenAgesOrderChanges() {
        Search command = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(AGES)
                .build();

        List<Integer> reversedAges = List.of(25, 30);
        Search commandWithReversedAges = Search.builder()
                .hotelId(HOTEL_ID)
                .checkIn(CHECK_IN)
                .checkOut(CHECK_OUT)
                .ages(reversedAges)
                .build();

        when(searchHashServicePort.generateHash(command)).thenReturn(GENERATED_HASH);
        when(searchHashServicePort.generateHash(commandWithReversedAges)).thenReturn(DIFFERENT_HASH);

        SearchId firstResult = useCase.execute(command);
        SearchId secondResult = useCase.execute(commandWithReversedAges);

        assertNotEquals(firstResult.value(), secondResult.value());
    }
}