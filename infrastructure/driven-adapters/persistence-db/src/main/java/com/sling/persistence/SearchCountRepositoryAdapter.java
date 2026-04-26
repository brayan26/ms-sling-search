package com.sling.persistence;

import com.sling.model.search.port.SearchCountRepositoryPort;
import com.sling.persistence.entities.SearchCountEntity;
import com.sling.persistence.search.repositories.JpaSearchCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SearchCountRepositoryAdapter implements SearchCountRepositoryPort {
    private final JpaSearchCountRepository jpaSearchCountRepository;

    @Override
    public long getCount(String hash) {
        return jpaSearchCountRepository.findById(hash)
                .map(SearchCountEntity::getCount)
                .orElse(0L);
    }

    @Override
    public void increment(String hash) {
        jpaSearchCountRepository.increment(hash);
    }
}
