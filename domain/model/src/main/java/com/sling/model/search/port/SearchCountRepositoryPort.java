package com.sling.model.search.port;

public interface SearchCountRepositoryPort {
    long getCount(String hash);

    void increment(String hash);
}
