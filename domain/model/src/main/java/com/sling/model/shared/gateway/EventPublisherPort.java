package com.sling.model.shared.gateway;

import com.sling.model.search.Search;

@FunctionalInterface
public interface EventPublisherPort {
    void publish(Search search);
}
