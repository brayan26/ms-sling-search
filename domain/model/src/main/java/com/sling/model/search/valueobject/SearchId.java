package com.sling.model.search.valueobject;

public record SearchId(String value) {
    public SearchId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SearchId cannot be null or blank");
        }
    }
}
