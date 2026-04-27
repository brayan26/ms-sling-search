package com.sling.model.search.valueobject;

public record SearchId(String searchId) {
    public SearchId {
        if (searchId == null || searchId.isBlank()) {
            throw new IllegalArgumentException("SearchId cannot be null or blank");
        }
    }
}
