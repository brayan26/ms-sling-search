package com.sling.web.search.dto.response;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
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
