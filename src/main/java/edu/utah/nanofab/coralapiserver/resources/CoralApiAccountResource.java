package edu.utah.nanofab.coralapiserver.resources;

import edu.nanofab.utah.coralapi.resource.Account;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.AccountOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.AccountOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.AccountOperationPut;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiAccountResource {
	
	private String coralIor;
	private String coralConfigUrl;
	private AtomicLong counter;
	public static final Logger logger = LoggerFactory.getLogger(CoralApiAccountResource.class);

    public CoralApiAccountResource(String coralIor, String coralConfigUrl ) {
        this.coralIor = coralIor;
        this.coralConfigUrl = coralConfigUrl;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Account getRequest(@QueryParam("name") Optional<String> name, @Auth User user) {
    	AccountOperationGet operation = new AccountOperationGet();
    	operation.init(this.coralIor, this.coralConfigUrl, name, Optional.<Object> absent(), user);
    	return (Account) (operation.perform());
    }
    
	@POST
    @Timed
    public Account createRequest(@Valid Account account, @Auth User user) {
    	AccountOperationPost operation = new AccountOperationPost();
    	operation.init(this.coralIor, 
    			this.coralConfigUrl, 
    			Optional.<String> absent(), 
    			Optional.<Object>of( account), 
    			user);
    	return (Account) (operation.perform());
    }
    
    @PUT
    @Timed
    public Account updateRequest(@Valid Account account, @Auth User user) {
    	AccountOperationPut operation = new AccountOperationPut();
    	operation.init(this.coralIor, 
    			this.coralConfigUrl, 
    			Optional.<String> absent(), 
    			Optional.<Object>of( account), 
    			user);
    	return (Account) (operation.perform());
    }

}
