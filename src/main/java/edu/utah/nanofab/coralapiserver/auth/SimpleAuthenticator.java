package edu.utah.nanofab.coralapiserver.auth;


import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapiserver.TokenConfiguration;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {
    private TokenConfiguration[] tokens;
	private ConcurrentHashMap<String, String> sessionTokens;
	private String coralIor;
	private String coralConfigUrl;

	public SimpleAuthenticator(TokenConfiguration[] tokens, 
			ConcurrentHashMap<String, String> sessionTokens, 
			String coralIor, 
			String coralConfigUrl) {
		super();
		this.tokens = tokens;
		this.sessionTokens = sessionTokens;
		this.coralIor = coralIor;
		this.coralConfigUrl = coralConfigUrl;
	}

	@Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
		if (credentials.getUsername().equals("auth-token")) {
                    Optional<User> tokensUser = authenticateByToken(credentials.getPassword());
                    
                    if (isValidUser(tokensUser)) {
                        return tokensUser;
                    }
                    else {
                        throw new AuthenticationException("Invalid user");
                    }
		} else {
			return authenticateByUsernamePassword(credentials.getUsername(), credentials.getPassword());
		}
    }
    
    public boolean isValidUser(Optional<User> user) {
        if (!user.isPresent())
            return false;
        
        String username = user.get().getUsername();
        CoralAPI api = new CoralAPI(username, this.coralIor, this.coralConfigUrl);
        try {
            api.getMember(username);
        } catch (Exception ex) {
            return false;
        } finally {
            api.close();
        }
        
        return true;
    }
	
	public Optional<User> authenticateByUsernamePassword(String user, String pass) {
		CoralAPI api = new CoralAPI(user, this.coralIor, this.coralConfigUrl);
		try {
			boolean success = api.authenticate(user, pass);
			api.close();
			if (success) {
				return Optional.of(new User(user));
			}
		} catch (Exception e) {
			api.close();
			e.printStackTrace();
		} finally {
			api.close();
		}
		return Optional.<User>absent();
	}
	
	public Optional<User> authenticateByToken(String requestToken) {
		for (TokenConfiguration t : tokens) {
			if ( t.getToken().equals(requestToken)) {
				return Optional.of(new User(t.getUser()));
			}
		}
		return authenticateBySessionToken(requestToken);
	}

	private Optional<User> authenticateBySessionToken(String requestToken) {
		if (sessionTokens.containsKey(requestToken)){
			return Optional.of(new User(sessionTokens.get(requestToken)));
		}
		return Optional.<User>absent();
	}
}
