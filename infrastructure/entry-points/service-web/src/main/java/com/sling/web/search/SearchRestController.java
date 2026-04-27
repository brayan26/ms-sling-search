package com.sling.web.search;

import com.sling.usecase.search.CreateSearchUseCase;
import com.sling.usecase.search.GetSearchCountUseCase;
import com.sling.web.search.dto.SearchRequestDto;
import com.sling.web.search.dto.response.CountResponseDto;
import com.sling.web.search.mapper.SearchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchRestController implements SearchRestDefinition {
    private final GetSearchCountUseCase getSearchCountUseCase;
    private final CreateSearchUseCase createSearchUseCase;


    public ResponseEntity<?> create(@Valid @RequestBody SearchRequestDto dto) {
        return ResponseEntity.ok(createSearchUseCase.execute(SearchMapper.MAPPER.toDomain(dto)));
    }

    @GetMapping(path = "/count", produces = {"application/json"})
    public ResponseEntity<?> getSearchCount(@RequestParam(value = "searchId") String searchId) {
        CountResponseDto response = CountResponseDto.builder()
                .searchId(searchId)
                .count(getSearchCountUseCase.execute(searchId))
                .build();
        return ResponseEntity.ok(response);
    }
}
