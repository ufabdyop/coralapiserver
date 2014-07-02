package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.resource.Enable;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.EnableRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.EnableOperationPost;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Path("/enable")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiEnableResource {
	
	private String coralIor;
	private String coralConfigUrl;
	private AtomicLong counter;
	public static final Logger logger = LoggerFactory.getLogger(CoralApiEnableResource.class);

    public CoralApiEnableResource(String coralIor, String coralConfigUrl ) {
        this.coralIor = coralIor;
        this.coralConfigUrl = coralConfigUrl;
        this.counter = new AtomicLong();
    }

    @POST
    @Timed
    public Enable createRequest(@Valid EnableRequest enableRequest, @Auth User user) {
    	EnableOperationPost operation = new EnableOperationPost();
    	operation.init(this.coralIor, 
    			this.coralConfigUrl, 
    			Optional.<String> absent(), 
    			Optional.<Object>of( enableRequest), 
    			user);
    	return (Enable) (operation.perform());
    }
    
}
