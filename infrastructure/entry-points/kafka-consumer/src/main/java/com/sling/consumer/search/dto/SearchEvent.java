package com.sling.consumer.search.dto;

import java.time.LocalDate;
import java.util.List;

public record SearchEvent(
        String hash,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {}
