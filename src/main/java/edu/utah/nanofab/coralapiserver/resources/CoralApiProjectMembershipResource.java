package edu.utah.nanofab.coralapiserver.resources;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectMembershipOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectMembershipOperationPut;

@Path("/project-membership")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiProjectMembershipResource {
	private String coralIor;
	private String coralConfigUrl;

	public CoralApiProjectMembershipResource(String coralIor,
			String coralConfigUrl) {
		this.coralIor = coralIor;
		this.coralConfigUrl = coralConfigUrl;
	}

	@GET
    @Timed
    public ProjectMembership get(@QueryParam("project") Optional<String> project, @Auth User user) {
    	ProjectMembershipOperationGet operation = new ProjectMembershipOperationGet();
    	operation.init(this.coralIor, this.coralConfigUrl, project, Optional.<Object> absent(), user);
    	return (ProjectMembership) (operation.perform());
    }
	
	@PUT
    @Timed
    public ProjectMembership update(@Valid ProjectMembership request, @Auth User user) {
    	ProjectMembershipOperationPut operation = new ProjectMembershipOperationPut();
    	operation.init(this.coralIor, 
    			this.coralConfigUrl,  
    			Optional.<String> absent(), 
    			Optional.<Object> of(request), 
    			user);
    	return (ProjectMembership) (operation.perform());
    }

}