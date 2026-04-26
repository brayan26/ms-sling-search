package com.sling.model.search.port;

import com.sling.model.search.Search;

@FunctionalInterface
public interface EventPublisherPort {
    void publish(Search search);
}
