package edu.utah.nanofab.coralapiserver.auth;


import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import edu.utah.nanofab.coralapi.CoralAPIPool;
import edu.utah.nanofab.coralapi.CoralAPISynchronized;
import edu.utah.nanofab.coralapiserver.TokenConfiguration;
import java.util.concurrent.atomic.AtomicLong;


public class SimpleAuthenticator implements Authenticator<BasicCredentials, User> {
    public static String GlobalLock = "coral api not thread safe";
    
    private TokenConfiguration[] tokens;
  private ConcurrentHashMap<String, TokenConfiguration> sessionTokens;
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(SimpleAuthenticator.class);   
    private final CoralAPIPool apiPool;

  public SimpleAuthenticator(TokenConfiguration[] tokens, 
      ConcurrentHashMap<String, TokenConfiguration> sessionTokens, 
      CoralAPIPool apiPool) {
    super();
    this.tokens = tokens;
    this.sessionTokens = sessionTokens;
    this.coralConfigUrl = coralConfigUrl;
    this.apiPool = apiPool;
    new AtomicLong();
  }

  @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
	    if (credentials.getUsername().equals("auth-token")) {
	                    Optional<User> tokensUser = authenticateByToken(credentials.getPassword());
	                    
	                    //special case for proxyAuthenticator
	                    if (!tokensUser.isPresent()) {
		                      logger.debug("User did not validate against coral: " + credentials.getPassword());
		                      return Optional.<User>absent();
	                    } else if ( tokensUser.get().getUsername().equals("proxyAuthenticator")) {
	                      logger.debug("bypassing coral check for proxyAuthenticator");
	                      return tokensUser;
	                    } else if (isValidUser(tokensUser)) {
	                        return tokensUser;
	                    } else {
	                      logger.debug("User did not validate against coral: " + tokensUser.get().getUsername());
	                      return Optional.<User>absent();
	                    }
	    } else {
	      return authenticateByUsernamePassword(credentials.getUsername(), credentials.getPassword());
	    }
    }
    
    public boolean isValidUser(Optional<User> user) {
        synchronized(GlobalLock) {
            if (!user.isPresent()) {
                return false;
            }

            String username = user.get().getUsername();
            CoralAPISynchronized coralApiInstance;
            
            try {
                coralApiInstance = apiPool.getConnection(username);
                coralApiInstance.getMember(username);
            } catch (Exception ex) {
                System.out.println("Authentication Failure: Error Follows");
                System.out.println(ex.getMessage());
                ex.printStackTrace(System.out);
                return false;
            } finally {
                //apiPool.closeConnection(username);
            }

            return true;
        }
    }
  
  public Optional<User> authenticateByUsernamePassword(String user, String pass) {
    synchronized(GlobalLock) {
        logger.debug("Authenticating " + user + " by password.");
        CoralAPISynchronized coralApiInstance;
        
        try {
          coralApiInstance = apiPool.getConnection(user);
          boolean success = coralApiInstance.authenticate(user, pass);
          //apiPool.closeConnection(user);
          if (success) {
            return Optional.of(new User(user));
          }
        } catch (Exception e) {
          apiPool.closeConnection(user);
          e.printStackTrace();
        } finally {
          //apiPool.closeConnection(user);
        }
        return Optional.<User>absent();
    }
  }
  
  public Optional<User> authenticateByToken(String requestToken) {
    logger.debug("Authenticating by token: " + requestToken);
    
    Date now = new Date();
    for (TokenConfiguration t : tokens) {
      if ( t.getToken().equals(requestToken)) {
        if (t.getExpiration().getTime() > now.getTime() ) {
          logger.debug("Token valid for " + t.getUser() + " until " + t.getExpiration());
          return Optional.of(new User(t.getUser()));
        } else {
          logger.debug("Token expired: " + t.getUser() + " on " + t.getExpiration());
        }
      }
    }
    logger.debug("Token not found in config file");
    
    return authenticateBySessionToken(requestToken);
  }

  private Optional<User> authenticateBySessionToken(String requestToken) {
    logger.debug("Checking session tokens");    
    Date now = new Date();
    if (sessionTokens.containsKey(requestToken)){
      TokenConfiguration token = sessionTokens.get(requestToken);
      if (token.getExpiration().getTime() > now.getTime()) {
        logger.debug("Token valid: " + token.getUser() + " until " + token.getExpiration());
        return Optional.of(new User(sessionTokens.get(requestToken).getUser()));
      } else {
        logger.debug("Token expired: " + token.getUser() + " on " + token.getExpiration());
      }
    }
    logger.debug("Token not found in session tokens");    
    return Optional.<User>absent();
  }
}
