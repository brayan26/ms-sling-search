package com.sling.web.search.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "Request de búsqueda de hoteles")
public record SearchRequestDto(
        @Schema(example = "HOTEL123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String hotelId,
        @Schema(example = "01/05/2026", pattern = "^\\d{2}\\/\\d{2}\\/\\d{4}$", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String checkIn,
        @Schema(example = "30/05/2026", pattern = "^\\d{2}\\/\\d{2}\\/\\d{4}$", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String checkOut,
        @Schema(example = "[30,4,23]", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty List<@NotNull Integer> ages
) {}
