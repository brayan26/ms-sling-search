package com.sling.web.search.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SearchRequestDto(
        @NotBlank String hotelId,
        @NotBlank String checkIn,
        @NotBlank String checkOut,
        @NotEmpty List<@NotNull Integer> ages
) {}
