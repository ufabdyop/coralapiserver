package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.collections.Projects;
import edu.utah.nanofab.coralapi.resource.Member;
import edu.utah.nanofab.coralapi.resource.Project;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.GenericResponse;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationPut;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectsOperationGet;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;

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

@Path("/v0/projects")
@Api(value = "/v0/projects", description = "")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiProjectsResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiProjectsResource.class);

  public CoralApiProjectsResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "Find projects by name or member", response = Project[].class)
  @Timed
  public Project[] getRequest(@QueryParam("name") Optional<String> name, 
		  @QueryParam("member") Optional<String> member, 
		  @Auth User user) {
	  Projects resultSet = new Projects();
	  Project[] resultSetAsArray;
	  if (name.isPresent()) {
		  logger.debug("projects query by name");
		  resultSet.add(getProjectByName(name.get(), user));
	  } else if (member.isPresent()) {
		  logger.debug("projects query by member");
		  resultSet = getProjectsByMember(member.get(), user);
	  } else {
		  logger.debug("projects query for all projects");
		  resultSet = getAllActiveProjects(user);
	  }
	  resultSetAsArray = resultSet.toArray();
	  logger.debug("returning project array after converting, length: " + resultSetAsArray.length);
	  return resultSetAsArray;
  }

  @POST
  @ApiOperation(value = "Update project", response = GenericResponse.class)
  @Timed
  public GenericResponse createRequest(@Valid Project project, @Auth User user) {
    ProjectOperationPost operation = new ProjectOperationPost();
    operation.init(
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( project), 
        user);
    operation.perform();
    GenericResponse response = new GenericResponse(true);
    return response;
  }
  
  @PUT
  @ApiOperation(value = "Create project", response = GenericResponse.class)
  @Timed
  public GenericResponse updateRequest(@Valid Project project, @Auth User user) {
	    ProjectOperationPut operation = new ProjectOperationPut();
	    operation.init(
	        this.coralConfigUrl, 
	        Optional.<String> absent(), 
	        Optional.<Object>of( project), 
	        user);
	    operation.perform();
	    GenericResponse response = new GenericResponse(true);
	    return response;
  }
  
  private Projects getProjectsByMember(String member, User apiUser) {
    ProjectsOperationGet operation = new ProjectsOperationGet();
    operation.init(this.coralConfigUrl, 
    		Optional.<String>of(member), 
    		Optional.<Object> absent(), 
    		apiUser);
    return (Projects) (operation.perform());
  }
  private Projects getAllActiveProjects(User apiUser) {
    ProjectsOperationGet operation = new ProjectsOperationGet();
    operation.init(this.coralConfigUrl, 
    		Optional.<String>absent(), 
    		Optional.<Object>absent(), 
    		apiUser);
    return (Projects) (operation.perform());
  }
  private Project getProjectByName(String name, User user) {
		ProjectOperationGet operation = new ProjectOperationGet();
	    operation.init( this.coralConfigUrl, Optional.<String>of(name), Optional.<Object> absent(), user);
	    return (Project) (operation.perform());
	}
	 

}
