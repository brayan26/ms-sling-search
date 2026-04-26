package com.sling.persistence.config;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class DBSecret {
    private final String url;
    private final String username;
    private final String password;

}
