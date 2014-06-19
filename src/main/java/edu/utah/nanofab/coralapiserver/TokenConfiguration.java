package edu.utah.nanofab.coralapiserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class TokenConfiguration  extends Configuration {
    @JsonProperty
    private String token;
	
    @JsonProperty
    private String user;

    public String getUser() {
            return user;
    }

    public String getToken() {
            return token;
    }

    public void setToken(String token) {
            this.token = token;
    }

    public void setUser(String user) {
            this.user = user;
    }
}
