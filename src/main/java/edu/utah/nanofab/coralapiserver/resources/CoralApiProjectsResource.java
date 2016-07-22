package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapi.collections.Members;
import edu.utah.nanofab.coralapi.collections.Projects;
import edu.utah.nanofab.coralapi.resource.Member;
import edu.utah.nanofab.coralapi.resource.Project;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.GenericResponse;
import edu.utah.nanofab.coralapiserver.core.ProjectName;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationPut;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectsOperationGet;

import org.opencoral.idl.InvalidAccountSignal;
import org.opencoral.idl.InvalidNicknameSignal;
import org.opencoral.idl.InvalidTicketSignal;
import org.opencoral.idl.NotAuthorizedSignal;
import org.opencoral.idl.ProjectNotFoundSignal;
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

import java.util.Date;
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
  @ApiOperation(value = "Find projects", notes="Find projects by name or member")
  @Timed
  public Projects getRequest(@QueryParam("name") Optional<String> name, 
		  @QueryParam("member") Optional<String> member, 
		  @Auth User user) {
	  Projects resultSet = new Projects();
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
	  logger.debug("returning projects collection, length: " + resultSet.size());
	  return resultSet;
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
  
  @POST
  @ApiOperation(value = "Activate Project", response = ProjectName.class)
  @Path("/activate")
  @Timed
  public ProjectName activate(@Valid ProjectName project, @Auth User user) throws Exception {
	  	CoralAPI api = new CoralAPI(user.getUsername(), this.coralConfigUrl);
		try {
			api.activateProject(project.getProject());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Caught exception activating " + project.getProject());
			throw e;
		}
		return project;
  }
  
  @POST
  @ApiOperation(value = "Deactivate Project", response = ProjectName.class)
  @Path("/deactivate")
  @Timed
  public ProjectName deactivate(@Valid ProjectName project, @Auth User user) throws Exception {
	  	CoralAPI api = new CoralAPI(user.getUsername(), this.coralConfigUrl);
		try {
			Members members = api.getProjectMembers(project.getProject());
			api.removeProjectMembers(project.getProject(), members.getNames());
			api.deactivateProject(project.getProject());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Caught exception deactivating " + project.getProject());
			throw e;
		}
		return project;
  }

  
  @GET
  @ApiOperation(value = "Get Sample Project Resource", response = Project.class)
  @Path("/example")
  @Timed
  public Project getProjectRequest() {
	  Project sample = new Project(false, "My project", "Project Description", "project nickname", "local", "chemistry", "My PI", "My account", true, new Date(), new Date());
	  return sample;	  
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
