package com.sling.model.search.gateway;

import com.sling.model.search.Search;

public interface SearchHashServicePort {
    String generateHash(Search search);
}
