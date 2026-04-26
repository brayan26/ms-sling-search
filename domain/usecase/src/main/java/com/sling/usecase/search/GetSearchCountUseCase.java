package com.sling.usecase.search;

import com.sling.model.search.port.SearchCountRepositoryPort;
import com.sling.usecase.IUseCase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetSearchCountUseCase implements IUseCase<String, Long> {
    private final SearchCountRepositoryPort searchCountRepositoryPort;

    @Override
    public Long execute(String hash) {
        return searchCountRepositoryPort.getCount(hash);
    }
}
