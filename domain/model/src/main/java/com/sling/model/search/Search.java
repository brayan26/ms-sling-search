package com.sling.model.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public final class Search {
    private final String hotelId;
    private final String checkIn;
    private final String checkOut;
    private final List<Integer> ages;
    private final String hash;
}
