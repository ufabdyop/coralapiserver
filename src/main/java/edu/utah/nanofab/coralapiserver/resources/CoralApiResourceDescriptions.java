package edu.utah.nanofab.coralapiserver.resources;

import java.util.Date;

import io.dropwizard.auth.Auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.codahale.metrics.annotation.Timed;
import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import edu.utah.nanofab.coralapi.collections.Projects;
import edu.utah.nanofab.coralapi.resource.Project;

@Api(value = "/v0/resources", description = "")
@Path("/v0/resources")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiResourceDescriptions {

	  @GET
	  //@ApiOperation(value = "Get Sample Resources", response = Projects.class)
	  @ApiOperation(value = "Get Sample Resources", response = Projects.class)
	  @Timed
	  public Object getRequest(@QueryParam("name") Optional<String> name) {
		  Object returnValue = new Object();
		  if (name.isPresent() && "project".equalsIgnoreCase(name.get())) {
			  Project sample = new Project(false, "My project", "Project Description", "project nickname", "local", "chemistry", "My PI", "My account", true, new Date(), new Date());
			  returnValue = sample;
		  }
		  return returnValue;
	  }
	
}
