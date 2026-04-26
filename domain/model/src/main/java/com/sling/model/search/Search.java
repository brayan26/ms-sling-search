package com.sling.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public final class Search {
    private final String hotelId;
    private final LocalDate checkIn;
    private final LocalDate checkOut;
    private final List<Integer> ages;
    private final String hash;
}
