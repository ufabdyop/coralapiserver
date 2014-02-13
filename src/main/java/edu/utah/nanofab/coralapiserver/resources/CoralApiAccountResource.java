package edu.utah.nanofab.coralapiserver.resources;

import edu.nanofab.coralapi.CoralServices;
import edu.nanofab.coralapi.resource.Member;
import edu.nanofab.coralapi.resource.Account;
import edu.utah.nanofab.coralapiserver.TokenConfiguration;
import edu.utah.nanofab.coralapiserver.auth.CoralCredentials;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.AuthRequest;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiAccountResource {
	
    private final String coralIor;
    private final String coralConfigUrl;
    private final AtomicLong counter;
    public static final Logger logger = LoggerFactory.getLogger(CoralApiAccountResource.class);

    public CoralApiAccountResource(String coralIor, String coralConfigUrl ) {
        this.coralIor = coralIor;
        this.coralConfigUrl = coralConfigUrl;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Account account(@QueryParam("name") Optional<String> name, @Auth User user) {
    	Account fetchedAccount = null;
		try {
			logger.debug("Will look up account in coral");
	    	if (name.isPresent()) {
	    		logger.debug("name is present and is " + name.get());
	    		CoralServices api = new CoralServices(user.getUsername(), 
	    				this.coralIor, this.coralConfigUrl);

	    		logger.debug("coral api instantiated");
				fetchedAccount = api.getAccount(name.get());
	    		logger.debug("account fetched" + (fetchedAccount == null ? ", but is null" : ": " + fetchedAccount.getName() ) );
				api.close();
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return fetchedAccount;
    }
    
    @POST
    @Timed
    public Account create(@Valid Account account, @Auth User user) {
    	Account fetchedAccount = null;
    	try {
			logger.debug("Adding new account in coral: " + account.getName());
    		CoralServices api = new CoralServices(user.getUsername(), 
    				this.coralIor, this.coralConfigUrl);

    		logger.debug("coral api instantiated");
    		api.CreateNewAccount(account);
    		fetchedAccount = api.getAccount(account.getName());
    		logger.debug("account fetched" + (fetchedAccount == null ? ", but is null" : ": " + fetchedAccount.getName() ) );
			api.close();
		} catch (Exception e) {
			e.printStackTrace();
		    Response resp = new ResponseBuilderImpl().status(500)
                    .entity("Error while trying to create account with name \"" + account.getName() + "\": "  + e.getMessage()).build();
			throw new WebApplicationException(resp);
		}
		return fetchedAccount;
    }
    
    @PUT
    @Timed
    public Account update(@Valid Account account, @Auth User user) {
    	Account fetchedAccount = null;
    	try {
			logger.debug("Adding new account in coral: " + account.getName());
    		CoralServices api = new CoralServices(user.getUsername(), 
    				this.coralIor, this.coralConfigUrl);

    		logger.debug("coral api instantiated");
    		api.updateAccount(account);
    		fetchedAccount = api.getAccount(account.getName());
    		logger.debug("account fetched" + (fetchedAccount == null ? ", but is null" : ": " + fetchedAccount.getName() ) );
			api.close();
    	} catch (Exception e) {
			e.printStackTrace();
		    Response resp = new ResponseBuilderImpl().status(500)
                    .entity("Error while trying to update account with name \"" + account.getName() + "\": "  + e.getMessage()).build();
			throw new WebApplicationException(resp);
		}
		return fetchedAccount;
    }
}
