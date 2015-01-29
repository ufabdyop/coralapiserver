package edu.utah.nanofab.coralapiserver;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class TokenConfiguration  extends Configuration {
   
  @JsonProperty
    private String token;
  
    @JsonProperty
    private String user;
    
    @JsonProperty
    private Date expiration;

    public TokenConfiguration() {
    super();
    expiration = defaultExpiration();
  }

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

  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }

  private Date defaultExpiration() {
    Date expiration = new Date();
    long millisecondsInDay = 24 * 60 * 60 * 1000;
    expiration.setTime(expiration.getTime() + millisecondsInDay);
    return expiration;
  } 
}
