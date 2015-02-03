package edu.utah.nanofab.coralapiserver;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CoralApiConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String coralIor = "http://coral-dev-box/IOR/";

    @NotEmpty
    @JsonProperty
    private String coralConfigUrl = "http://coral-dev-box/coral/lib/config.jar";

    @NotEmpty
    @JsonProperty
    private String logLevel = "DEBUG";  // Set the default log level to DEBUG if none is provided
                      // in the configuration file.
    
    @JsonProperty
    public String getCoralConfigUrl() {
        return this.coralConfigUrl;
    }

    @JsonProperty
    public String getCoralIor() {
        return this.coralIor;
    }
    
    @JsonProperty
    public String getLogLevel() {
      return this.logLevel;
    }
    
    @JsonProperty
    private AuthTokensConfiguration authToken = new AuthTokensConfiguration();

    public AuthTokensConfiguration getAuthTokensConfiguration() {
        return authToken;
    }
}
