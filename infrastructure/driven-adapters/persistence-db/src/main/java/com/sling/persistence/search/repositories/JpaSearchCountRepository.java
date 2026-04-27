package com.sling.persistence.search.repositories;

import com.sling.persistence.entities.SearchCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSearchCountRepository extends JpaRepository<SearchCountEntity, String> {
    @Modifying
    @Query(value = """
                INSERT INTO search_count (hash, count)
                VALUES (:hash, 1)
                ON CONFLICT (hash)
                DO UPDATE SET count = search_count.count + 1
            """, nativeQuery = true)
    void increment(@Param(value = "hash") String hash);
}
