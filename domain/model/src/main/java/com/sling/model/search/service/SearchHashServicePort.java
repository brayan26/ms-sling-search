package com.sling.model.search.service;

import com.sling.model.search.Search;

public interface SearchHashServicePort {
    String generateHash(Search search);
}
