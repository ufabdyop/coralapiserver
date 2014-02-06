package edu.utah.nanofab.coralapiserver.auth;


import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

import edu.utah.nanofab.coralapiserver.TokenConfiguration;


public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {
    private TokenConfiguration[] tokens;

	public SimpleAuthenticator(TokenConfiguration[] tokens) {
		super();
		this.tokens = tokens;
	}

	@Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
		if (credentials.getUsername().equals("auth-token")) {
	        return authenticateByToken(credentials.getPassword());
		} else {
			return Optional.<User>absent();
		}
    }
	
	public Optional<User> authenticateByToken(String requestToken) {
		for (TokenConfiguration t : tokens) {
			if ( t.getToken().equals(requestToken)) {
				return Optional.of(new User(t.getUser()));
			}
		}
		return Optional.<User>absent();
	}
}