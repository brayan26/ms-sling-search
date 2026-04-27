package com.sling.web.search.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Schema(description = "Cantidad de búsquedas por identificador")
@Builder(toBuilder = true)
public record CountResponseDto(
        @Schema(description = "Identificador de la búsqueda", example = "abc123hash")
        String searchId,
        SearchDto search,
        @Schema(description = "Número de veces que se ha ejecutado la búsqueda", example = "5")
        long count
) {
    public record SearchDto(
            String hotelId,
            String checkIn,
            String checkOut,
            List<Integer> ages
    ) {}
}
