package edu.utah.nanofab.coralapiserver.resources;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import edu.nanofab.utah.coralapi.CoralAPI;
import edu.nanofab.utah.coralapi.resource.Member;
import edu.utah.nanofab.coralapiserver.auth.CoralCredentials;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.AuthRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationGet;

@Path("/authenticate")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiAuthTokenResource {
	private ConcurrentHashMap<String, String> sessionTokens;
	private String coralIor;
	private String coralConfigUrl;
	
	public CoralApiAuthTokenResource(String coralIor, String coralConfigUrl, ConcurrentHashMap<String, String> sessionTokens) {
        this.coralIor = coralIor;
        this.coralConfigUrl = coralConfigUrl;
        this.sessionTokens = sessionTokens;
    }
	
    @GET
    @Timed
    public Response getRequest(@Auth User user) {
    	return generateResponse(true, user.getUsername());
    }

    @POST
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
		this.sessionTokens.put(sessionId, username);
		sessionToken = new CoralCredentials();
		sessionToken.setUsername("auth-token");
		sessionToken.setPassword(sessionId);
		return sessionToken;
	}


	private boolean authenticate(String username, String password) {
		CoralAPI api = new CoralAPI(username, 
				this.coralIor, this.coralConfigUrl);
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
