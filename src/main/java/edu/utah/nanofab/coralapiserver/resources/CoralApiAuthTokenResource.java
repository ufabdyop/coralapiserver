package edu.utah.nanofab.coralapiserver.resources;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapiserver.TokenConfiguration;
import edu.utah.nanofab.coralapiserver.auth.CoralCredentials;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.AuthRequest;

@Path("/v0/authenticate")
@Api(value = "/v0/authenticate", description = "")

@Produces(MediaType.APPLICATION_JSON)
public class CoralApiAuthTokenResource {
  private ConcurrentHashMap<String, TokenConfiguration> sessionTokens;
  private String coralConfigUrl;
  
  public CoralApiAuthTokenResource(String coralConfigUrl, ConcurrentHashMap<String, TokenConfiguration> sessionTokens) {
        this.coralConfigUrl = coralConfigUrl;
        this.sessionTokens = sessionTokens;
  }
  
    @GET
    @ApiOperation(value = "")
    @Timed
    public Response getRequest(@QueryParam("proxyFor") Optional<String> name, @Auth User user) {
      //default response of unauthorized
      Response response = Response.status(Response.Status.UNAUTHORIZED).build();

      if (name.isPresent()) {
        //perform proxy auth and return token only if auth'd user is "proxyAuthenticator"
        if (user.getUsername().equals("proxyAuthenticator")) {
            response = generateResponse(true, name.get());
        }
      } else {
          //register token for user authenticating
        response = generateResponse(true, user.getUsername());
      }
      return response;
    }

    @POST
    @ApiOperation(value = "")    
    public Response authRequest(@Valid AuthRequest authRequest) {
      String username = authRequest.getUsername();
      String password = authRequest.getPassword();
      return validateUsernamePassword(username, password); 
    }

    private Response validateUsernamePassword(String username, String password) {
      boolean success = authenticate(username, password);
      return generateResponse(success, username);
    }
    
  /**
   * Generates the proper response for a successful or failed auth token request
   * @param success   true if the response is for a successful auth attempt, false returns a 401
   * @param username  username requesting access
   * @return Response
   */
  private Response generateResponse(boolean success, String username) {
    CoralCredentials sessionToken = new CoralCredentials();
    sessionToken.setUsername("Auth Failed");

    if (success) {
      sessionToken = createUserToken(username);
      return Response.status(Response.Status.OK).entity(sessionToken).build(); 
    }
    return Response.status(Response.Status.UNAUTHORIZED).entity(sessionToken).build();
  }

  private CoralCredentials createUserToken(String username) {
    CoralCredentials sessionToken;
    String sessionId;
    sessionId = randomSessionId();
    TokenConfiguration t = new TokenConfiguration();
    t.setUser(username);
    t.setToken(sessionId);
    t.setExpiration(defaultExpiration());
    this.sessionTokens.put(sessionId, t);
    sessionToken = new CoralCredentials();
    sessionToken.setUsername("auth-token");
    sessionToken.setPassword(sessionId);
    return sessionToken;
  }

  private Date defaultExpiration() {
    Date expiration = new Date();
    long millisecondsInDay = 24 * 60 * 60 * 1000;
    expiration.setTime(expiration.getTime() + millisecondsInDay);
    return expiration;
  }

  private boolean authenticate(String username, String password) {
    CoralAPI api = new CoralAPI(username, this.coralConfigUrl);
    boolean success = false;
    try {
      success = api.authenticate(username, password);
      api.close();
    } catch (Exception e) {
      api.close();
      e.printStackTrace();
    }
    return success;
  }


  private String randomSessionId() {
    SecureRandom random = new SecureRandom();
    return new BigInteger(130, random).toString(32);
  }

}
