package com.sling.hash;

import com.sling.model.search.Search;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class Sha256SearchHashServiceTest {

    private static final String HOTEL_ID = "1234";
    private static final String CHECK_IN = "2026-05-01";
    private static final String CHECK_OUT = "2026-05-05";
    private static final List<Integer> AGES = List.of(30, 25);
    private static final String DIFFERENT_HOTEL_ID = "5678";
    private static final String DIFFERENT_CHECK_IN = "2026-06-01";
    private static final List<Integer> DIFFERENT_AGES = List.of(40);
    private static final String SEPARATOR = "|";

    private Sha256SearchHashService hashService;

    @BeforeEach
    void setUp() {
        hashService = new Sha256SearchHashService();
    }

    @Nested
    @DisplayName("generateHash - Generates a SHA-256 hash from Search fields")
    class GenerateHashTests {

        @Test
        @DisplayName("Should return a 64-character hexadecimal hash for a valid search")
        void shouldReturnHexHashWith64CharactersForValidSearch() {
            Search search = buildSearch(HOTEL_ID, CHECK_IN, AGES);

            String hash = hashService.generateHash(search);

            assertNotNull(hash);
            assertEquals(64, hash.length());
            assertEquals(hash, hash.toLowerCase());
        }

        @Test
        @DisplayName("Should return the expected SHA-256 hash matching manual computation")
        void shouldReturnExpectedSha256HashMatchingManualComputation() throws Exception {
            Search search = buildSearch(HOTEL_ID, CHECK_IN, AGES);
            String canonical = HOTEL_ID + SEPARATOR + CHECK_IN + SEPARATOR + CHECK_OUT + SEPARATOR + "30,25";
            String expected = manualSha256(canonical);

            String hash = hashService.generateHash(search);

            assertEquals(expected, hash);
        }

        @Test
        @DisplayName("Should return the same hash when invoked twice with identical search data")
        void shouldReturnSameHashForIdenticalSearchData() {
            Search search1 = buildSearch(HOTEL_ID, CHECK_IN, AGES);
            Search search2 = buildSearch(HOTEL_ID, CHECK_IN, AGES);

            String hash1 = hashService.generateHash(search1);
            String hash2 = hashService.generateHash(search2);

            assertEquals(hash1, hash2);
        }

        @Test
        @DisplayName("Should return different hashes when hotelId differs")
        void shouldReturnDifferentHashWhenHotelIdDiffers() {
            Search search1 = buildSearch(HOTEL_ID, CHECK_IN, AGES);
            Search search2 = buildSearch(DIFFERENT_HOTEL_ID, CHECK_IN, AGES);

            assertNotEquals(hashService.generateHash(search1), hashService.generateHash(search2));
        }

        @Test
        @DisplayName("Should return different hashes when checkIn differs")
        void shouldReturnDifferentHashWhenCheckInDiffers() {
            Search search1 = buildSearch(HOTEL_ID, CHECK_IN, AGES);
            Search search2 = buildSearch(HOTEL_ID, DIFFERENT_CHECK_IN, AGES);

            assertNotEquals(hashService.generateHash(search1), hashService.generateHash(search2));
        }

        @Test
        @DisplayName("Should return different hashes when ages list differs")
        void shouldReturnDifferentHashWhenAgesDiffer() {
            Search search1 = buildSearch(HOTEL_ID, CHECK_IN, AGES);
            Search search2 = buildSearch(HOTEL_ID, CHECK_IN, DIFFERENT_AGES);

            assertNotEquals(hashService.generateHash(search1), hashService.generateHash(search2));
        }

        @Test
        @DisplayName("Should handle single age in the list correctly")
        void shouldHandleSingleAgeInListCorrectly() throws Exception {
            List<Integer> singleAge = List.of(18);
            Search search = buildSearch(HOTEL_ID, CHECK_IN, singleAge);
            String canonical = HOTEL_ID + SEPARATOR + CHECK_IN + SEPARATOR + CHECK_OUT + SEPARATOR + "18";
            String expected = manualSha256(canonical);

            String hash = hashService.generateHash(search);

            assertEquals(expected, hash);
        }
    }

    private Search buildSearch(String hotelId, String checkIn, List<Integer> ages) {
        return Search.builder()
                .hotelId(hotelId)
                .checkIn(checkIn)
                .checkOut(Sha256SearchHashServiceTest.CHECK_OUT)
                .ages(ages)
                .build();
    }

    private String manualSha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            String h = Integer.toHexString(0xff & b);
            if (h.length() == 1) hex.append('0');
            hex.append(h);
        }
        return hex.toString();
    }
}
