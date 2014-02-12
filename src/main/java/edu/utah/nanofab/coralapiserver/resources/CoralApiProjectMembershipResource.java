package edu.utah.nanofab.coralapiserver.resources;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.opencoral.idl.InvalidMemberSignal;
import org.opencoral.idl.InvalidProjectSignal;
import org.opencoral.idl.InvalidTicketSignal;
import org.opencoral.idl.NotAuthorizedSignal;

import com.google.common.base.Optional;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import edu.nanofab.coralapi.CoralServices;
import edu.nanofab.coralapi.collections.Members;
import edu.nanofab.coralapi.resource.Account;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;

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
		try {
			CoralServices api = new CoralServices(user.getUsername(), 
					this.coralIor, this.coralConfigUrl);
			return getProjectMembership(project.get(), api);
		} catch (Exception e) {
			e.printStackTrace();
		    Response resp = new ResponseBuilderImpl().status(500)
		    		.entity("Error while trying to add members to project: " + e.getMessage()).build();
			throw new WebApplicationException(resp);
		}
    }
	
	@PUT
    @Timed
    public ProjectMembership update(@Valid ProjectMembership request, @Auth User user) {
		try {
			CoralServices api = new CoralServices(user.getUsername(), 
				this.coralIor, this.coralConfigUrl);
			api.AddProjectMembers(request.getProject(), request.getMembers());
			return this.getProjectMembership(request.getProject(), api);
		} catch (Exception e) {
			e.printStackTrace();
		    Response resp = new ResponseBuilderImpl().status(500)
		    		.entity("Error while trying to add members to project: " + e.getMessage()).build();
			throw new WebApplicationException(resp);
		}
    }

	private ProjectMembership getProjectMembership(String project,
			CoralServices api) {
		Members members = 
				api.GetProjectMembers(project);
		String[] memberNames = members.getNames();
		ProjectMembership pm = new ProjectMembership();
		pm.setProject(project);
		pm.setMembers(memberNames);
		return pm;
	}
}
