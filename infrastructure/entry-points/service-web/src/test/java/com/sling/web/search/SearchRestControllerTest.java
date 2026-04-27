package com.sling.web.search;

import com.sling.model.search.Search;
import com.sling.model.search.valueobject.SearchId;
import com.sling.usecase.search.CreateSearchUseCase;
import com.sling.usecase.search.GetSearchCountUseCase;
import com.sling.web.search.dto.SearchRequestDto;
import com.sling.web.search.dto.response.CountResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchRestControllerTest {

    private static final String HOTEL_ID = "1234";
    private static final String CHECK_IN = "01/05/2026";
    private static final String CHECK_OUT = "05/05/2026";
    private static final List<Integer> AGES = List.of(30, 25);
    private static final String GENERATED_HASH = "abc123hash";
    private static final long EXPECTED_COUNT = 5L;

    @Mock
    private CreateSearchUseCase createSearchUseCase;

    @Mock
    private GetSearchCountUseCase getSearchCountUseCase;

    private SearchRestController controller;

    @BeforeEach
    void setUp() {
        controller = new SearchRestController(getSearchCountUseCase, createSearchUseCase);
    }

    @Nested
    @DisplayName("POST /api/v1/search - Creates a new search")
    class CreateSearchTests {

        @Test
        @DisplayName("Should return 200 OK with SearchId when request body is valid")
        void shouldReturnOkWithSearchIdWhenRequestIsValid() {
            SearchRequestDto dto = new SearchRequestDto(HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);
            when(createSearchUseCase.execute(any(Search.class))).thenReturn(new SearchId(GENERATED_HASH));

            ResponseEntity<?> response = controller.create(dto);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            SearchId body = (SearchId) response.getBody();
            assertEquals(GENERATED_HASH, body.searchId());
            verify(createSearchUseCase).execute(any(Search.class));
        }

        @Test
        @DisplayName("Should map SearchRequestDto fields correctly to Search domain object")
        void shouldMapDtoFieldsCorrectlyToDomainObject() {
            SearchRequestDto dto = new SearchRequestDto(HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);
            when(createSearchUseCase.execute(any(Search.class))).thenReturn(new SearchId(GENERATED_HASH));

            controller.create(dto);

            ArgumentCaptor<Search> captor = ArgumentCaptor.forClass(Search.class);
            verify(createSearchUseCase).execute(captor.capture());

            Search captured = captor.getValue();
            assertEquals(HOTEL_ID, captured.getHotelId());
            assertEquals(AGES, captured.getAges());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/count - Retrieves search count by searchId")
    class GetSearchCountTests {

        @Test
        @DisplayName("Should return 200 OK with searchId and count when searchId exists")
        void shouldReturnOkWithCountWhenSearchIdExists() {
            when(getSearchCountUseCase.execute(GENERATED_HASH)).thenReturn(EXPECTED_COUNT);

            ResponseEntity<?> response = controller.getSearchCount(GENERATED_HASH);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            CountResponseDto body = (CountResponseDto) response.getBody();
            assertEquals(GENERATED_HASH, body.searchId());
            assertEquals(EXPECTED_COUNT, body.count());
            verify(getSearchCountUseCase).execute(GENERATED_HASH);
        }

        @Test
        @DisplayName("Should return 200 OK with zero count when searchId has no searches")
        void shouldReturnOkWithZeroCountWhenNoSearchesExist() {
            when(getSearchCountUseCase.execute(GENERATED_HASH)).thenReturn(0L);

            ResponseEntity<?> response = controller.getSearchCount(GENERATED_HASH);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            CountResponseDto body = (CountResponseDto) response.getBody();
            assertNotNull(body);
            assertEquals(0L, body.count());
        }
    }

    @Nested
    @DisplayName("DTOs - Coverage for record constructors and accessors")
    class DtoCoverageTests {

        @Test
        @DisplayName("Should create SearchRequestDto with all fields accessible")
        void shouldCreateSearchRequestDtoWithAllFields() {
            SearchRequestDto dto = new SearchRequestDto(HOTEL_ID, CHECK_IN, CHECK_OUT, AGES);

            assertEquals(HOTEL_ID, dto.hotelId());
            assertEquals(CHECK_IN, dto.checkIn());
            assertEquals(CHECK_OUT, dto.checkOut());
            assertEquals(AGES, dto.ages());
        }

        @Test
        @DisplayName("Should create CountResponseDto with builder and access all fields")
        void shouldCreateCountResponseDtoWithBuilder() {
            CountResponseDto.SearchDto searchDto = new CountResponseDto.SearchDto(
                    HOTEL_ID, CHECK_IN, CHECK_OUT, AGES
            );
            CountResponseDto dto = CountResponseDto.builder()
                    .searchId(GENERATED_HASH)
                    .search(searchDto)
                    .count(EXPECTED_COUNT)
                    .build();

            assertEquals(GENERATED_HASH, dto.searchId());
            assertEquals(EXPECTED_COUNT, dto.count());
            assertNotNull(dto.search());
            assertEquals(HOTEL_ID, dto.search().hotelId());
            assertEquals(CHECK_IN, dto.search().checkIn());
            assertEquals(CHECK_OUT, dto.search().checkOut());
            assertEquals(AGES, dto.search().ages());
        }

        @Test
        @DisplayName("Should create CountResponseDto using toBuilder and modify fields")
        void shouldCreateCountResponseDtoUsingToBuilder() {
            CountResponseDto original = CountResponseDto.builder()
                    .searchId(GENERATED_HASH)
                    .count(EXPECTED_COUNT)
                    .build();

            CountResponseDto modified = original.toBuilder()
                    .count(10L)
                    .build();

            assertEquals(GENERATED_HASH, modified.searchId());
            assertEquals(10L, modified.count());
        }
    }
}
