package edu.utah.nanofab.coralapiserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

public class AuthTokensConfiguration extends Configuration {
    
    @JsonProperty
    private TokenConfiguration[] tokens;

    @JsonProperty
    public TokenConfiguration[] getTokens() {
        return tokens;
    }
}