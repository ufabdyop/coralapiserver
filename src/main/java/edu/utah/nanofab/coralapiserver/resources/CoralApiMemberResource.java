package edu.utah.nanofab.coralapiserver.resources;

import edu.nanofab.coralapi.CoralServices;
import edu.nanofab.coralapi.resource.Account;
import edu.nanofab.coralapi.resource.Member;
import edu.nanofab.coralapi.resource.Project;
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

@Path("/member")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiMemberResource {
    private final String coralIor;
    private final String coralConfigUrl;
    private final AtomicLong counter;
    public static final Logger logger = LoggerFactory.getLogger(CoralApiMemberResource.class);

    public CoralApiMemberResource(String coralIor, String coralConfigUrl ) {
        this.coralIor = coralIor;
        this.coralConfigUrl = coralConfigUrl;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Member member(@QueryParam("name") Optional<String> name, @Auth User user) {
    	Member fetchedMember = null;
		try {
			logger.debug("Will look up member in coral");
	    	if (name.isPresent()) {
	    		logger.debug("name is present and is " + name.get());
	    		CoralServices api = new CoralServices(user.getUsername(), 
	    				this.coralIor, this.coralConfigUrl);

	    		logger.debug("coral api instantiated");
				fetchedMember = api.getMember(name.get());
	    		logger.debug("member fetched" + (fetchedMember == null ? ", but is null" : ": " + fetchedMember.getName() ) );
	    	}
		} catch (Exception e) {
			e.printStackTrace();
		}
        return fetchedMember;
    }
    
    @POST
    @Timed
    public Member create(@Valid Member member, @Auth User user) {
    	Member fetchedMember = null;
    	try {
			logger.debug("Adding new member in coral: " + member.getName());
    		CoralServices api = new CoralServices(user.getUsername(), 
    				this.coralIor, this.coralConfigUrl);

    		logger.debug("coral api instantiated");
    		api.CreateNewMember(member);
    		fetchedMember = api.getMember(member.getName());
    		logger.debug("member fetched" + (fetchedMember == null ? ", but is null" : ": " + fetchedMember.getName() ) );
		} catch (Exception e) {
			e.printStackTrace();
		    Response resp = new ResponseBuilderImpl().status(500)
                    .entity("Error while trying to create member with name \"" + member.getName() + "\": "  + e.getMessage()).build();
			throw new WebApplicationException(resp);
		}
		return fetchedMember;
    }

    
    @PUT
    @Timed
    public Member update(@Valid Member member, @Auth User user) {
    	Member fetchedMember = null;
    	try {
			logger.debug("Adding new member in coral: " + member.getName());
    		CoralServices api = new CoralServices(user.getUsername(), 
    				this.coralIor, this.coralConfigUrl);

    		logger.debug("coral api instantiated");
    		api.updateMember(member);
    		fetchedMember = api.getMember(member.getName());
    		logger.debug("member fetched" + (fetchedMember == null ? ", but is null" : ": " + fetchedMember.getName() ) );
    	} catch (Exception e) {
			e.printStackTrace();
		    Response resp = new ResponseBuilderImpl().status(500)
                    .entity("Error while trying to update member with name \"" + member.getName() + "\": "  + e.getMessage()).build();
			throw new WebApplicationException(resp);
		}
		return fetchedMember;
    }

}
