package edu.utah.nanofab.coralapiserver.resources;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.nanofab.coralapi.CoralServices;
import edu.utah.nanofab.coralapiserver.auth.CoralCredentials;
import edu.utah.nanofab.coralapiserver.core.AuthRequest;

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

    @POST
    public Response authRequest(@Valid AuthRequest authRequest) {
    	CoralCredentials sessionToken = new CoralCredentials();
    	sessionToken.setUsername("Auth Failed");
    	String sessionId = "";
		try {
			CoralServices api = new CoralServices(authRequest.getUsername(), 
					this.coralIor, this.coralConfigUrl);
			boolean success = api.authenticate(authRequest.getUsername(), authRequest.getPassword());
			if (success) {
				sessionId = randomSessionId();
				this.sessionTokens.put(sessionId, authRequest.getUsername());
				sessionToken = new CoralCredentials();
				sessionToken.setUsername("auth-token");
				sessionToken.setPassword(sessionId);
				return Response.status(Response.Status.OK).entity(sessionToken).build(); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.status(Response.Status.UNAUTHORIZED).entity(sessionToken).build(); 
     }
    
    private String randomSessionId() {
    	SecureRandom random = new SecureRandom();
	    return new BigInteger(130, random).toString(32);
    }

}
