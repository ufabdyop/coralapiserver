package edu.utah.nanofab.coralapiserver;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CoralApiConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String coralIor = "http://vagrant-coral-dev/IOR/";

    @NotEmpty
    @JsonProperty
    private String coralConfigUrl = "http://vagrant-coral-dev/coral/lib/config.jar";


    public String getCoralConfigUrl() {
        return coralConfigUrl;
    }

    public String getCoralIor() {
        return coralIor;
    }
    
    @JsonProperty
    private AuthTokensConfiguration authToken = new AuthTokensConfiguration();

    public AuthTokensConfiguration getAuthTokensConfiguration() {
        return authToken;
    }
}
