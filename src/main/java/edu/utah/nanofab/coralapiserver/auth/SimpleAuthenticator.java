package edu.utah.nanofab.coralapiserver.auth;


import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;


public class SimpleAuthenticator implements Authenticator<CoralCredentials, User> {
    @Override
    public Optional<User> authenticate(CoralCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {
            return Optional.of(new User(credentials.getUsername()));
        }
        return Optional.of(new User());
    }
}