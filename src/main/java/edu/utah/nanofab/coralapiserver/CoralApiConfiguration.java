package edu.utah.nanofab.coralapiserver;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CoralApiConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Stranger";

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }
    
    @JsonProperty
    private AuthTokensConfiguration authToken = new AuthTokensConfiguration();

    public AuthTokensConfiguration getAuthTokensConfiguration() {
        return authToken;
    }
}
