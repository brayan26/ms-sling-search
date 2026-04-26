package com.sling.model.shared.gateway;

import com.sling.model.search.valueobject.SearchId;

@FunctionalInterface
public interface EventPublisherPort {
    void publish(SearchId searchId);
}
