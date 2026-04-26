package com.sling.web.search.dto.response;

import java.util.List;

public record CountResponseDto(
        String searchId,
        SearchDto search,
        long count
) {
    public record SearchDto(
            String hotelId,
            String checkIn,
            String checkOut,
            List<Integer> ages
    ) {}
}
