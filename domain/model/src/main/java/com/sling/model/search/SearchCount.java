package com.sling.model.search;

import com.sling.model.search.valueobject.SearchId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public final class SearchCount {
    private final SearchId searchId;
    private final long count;
}