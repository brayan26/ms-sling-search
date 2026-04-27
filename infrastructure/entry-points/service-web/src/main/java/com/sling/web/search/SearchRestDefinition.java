package com.sling.web.search;

import com.sling.web.search.dto.SearchRequestDto;
import com.sling.web.search.dto.response.CountResponseDto;
import com.sling.web.search.dto.response.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface SearchRestDefinition {

    @Operation(
            summary = "Realizar búsqueda de hoteles",
            requestBody = @RequestBody(
                    required = true,
                    description = "Datos de la búsqueda",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SearchRequestDto.class)
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CountResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping(path = "/search", produces = "application/json")
    ResponseEntity<?> create(@Valid @org.springframework.web.bind.annotation.RequestBody SearchRequestDto dto);

    @Operation(
            summary = "Obtener cantidad de ejecuciones de una búsqueda",
            description = "Retorna el número de veces que una búsqueda ha sido ejecutada a partir de su identificador",
            parameters = {
                    @Parameter(
                            name = "searchId",
                            description = "Identificador único de la búsqueda",
                            required = true,
                            example = "abc123hash"
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Consulta exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CountResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Búsqueda no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping(path = "/count", produces = {"application/json"})
    ResponseEntity<?> getSearchCount(@RequestParam(value = "searchId") String searchId);
}
