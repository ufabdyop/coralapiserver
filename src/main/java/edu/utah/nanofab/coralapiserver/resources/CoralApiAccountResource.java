package edu.utah.nanofab.coralapiserver.resources;

import edu.nanofab.coralapi.CoralServices;
import edu.nanofab.coralapi.resource.Member;
import edu.nanofab.coralapi.resource.Account;
import edu.utah.nanofab.coralapiserver.TokenConfiguration;
import edu.utah.nanofab.coralapiserver.auth.CoralCredentials;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.AuthRequest;

import org.opencoral.idl.AccountNotFoundSignal;
import org.opencoral.idl.InvalidAccountSignal;
import org.opencoral.idl.InvalidTicketSignal;
import org.opencoral.idl.NotAuthorizedSignal;
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
	private CoralServices api;
	private String error;
    public static final Logger logger = LoggerFactory.getLogger(CoralApiAccountResource.class);

    public CoralApiAccountResource(String coralIor, String coralConfigUrl ) {
        this.coralIor = coralIor;
        this.coralConfigUrl = coralConfigUrl;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Account getRequest(@QueryParam("name") Optional<String> name, @Auth User user) {
		this.setUp(user.getUsername());
    	Account fetchedAccount = this.get(name.get());
		this.tearDown();
		this.reportErrorIfEncountered("Error while trying to fetch account with name \"" + name.get());
		return fetchedAccount;
    }
    
	@POST
    @Timed
    public Account createRequest(@Valid Account account, @Auth User user) {
		this.setUp(user.getUsername());
    	Account fetchedAccount = this.create(account);
		this.tearDown();
		this.reportErrorIfEncountered("Error while trying to create account with name \"" + account.getName());
		return fetchedAccount;
    }
    
    @PUT
    @Timed
    public Account updateRequest(@Valid Account account, @Auth User user) {
		this.setUp(user.getUsername());
    	Account fetchedAccount = this.update(account);
		this.tearDown();
		this.reportErrorIfEncountered("Error while trying to update account with name \"" + account.getName());
		return fetchedAccount;
    }
    
    private Account get(String accountName) {
		try {
			return api.getAccount(accountName);
		} catch (Exception e) {
			this.error = e.getMessage();
			return null;
		}
	}

	private Account create(Account account) {
		try {
			api.CreateNewAccount(account);
			return api.getAccount(account.getName());
		} catch (Exception e) {
			this.error = e.getMessage();
			return null;
		}
	}
    
	private Account update(Account account) {
		try {
			api.updateAccount(account);
			return api.getAccount(account.getName());
		} catch (Exception e) {
			this.error = e.getMessage();
			return null;
		}
	}

	public void setUp(String username) {
		this.error = null;
    	this.api = new CoralServices(username,
				this.coralIor, this.coralConfigUrl);
    }
    
    public void tearDown() {
    	this.api.close();
    	this.api = null;
    }

    private void reportErrorIfEncountered(String message) {
		if (this.error != null) {
		    Response resp = new ResponseBuilderImpl().status(500)
                    .entity(message + "\": "  + this.error).build();
			throw new WebApplicationException(resp);
		}
	}
}
