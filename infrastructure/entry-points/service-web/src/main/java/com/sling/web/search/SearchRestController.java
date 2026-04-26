package com.sling.web.search;

import com.sling.usecase.search.CreateSearchUseCase;
import com.sling.usecase.search.GetSearchCountUseCase;
import com.sling.web.search.dto.SearchRequestDto;
import com.sling.web.search.mapper.SearchMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SearchRestController {
    private final GetSearchCountUseCase getSearchCountUseCase;
    private final CreateSearchUseCase createSearchUseCase;

    @PostMapping(path = "/search", produces = {"application/json"})
    public ResponseEntity<?> create(@Valid @RequestBody SearchRequestDto dto) {
        return ResponseEntity.ok(createSearchUseCase.execute(SearchMapper.MAPPER.toDomain(dto)));
    }

    @GetMapping(path = "/count", produces = {"application/json"})
    public ResponseEntity<?> getSearchCount(@RequestParam String searchId) {
        return ResponseEntity.ok(getSearchCountUseCase.execute(searchId));
    }
}
