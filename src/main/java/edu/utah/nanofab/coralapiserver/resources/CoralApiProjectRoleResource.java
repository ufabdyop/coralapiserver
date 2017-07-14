package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.collections.ProjectRoles;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ProjectRoleRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectRoleOperationDelete;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectRoleOperationPost;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.utah.nanofab.coralapi.CoralAPIPool;

@Path("/v0/projectRoles")
@Api(value = "/v0/projectRoles", description = "")

@Produces(MediaType.APPLICATION_JSON)
public class CoralApiProjectRoleResource {
  
  public static final Logger logger = LoggerFactory.getLogger(CoralApiProjectRoleResource.class);
  private CoralAPIPool apiPool;
  
  public CoralApiProjectRoleResource(CoralAPIPool apiPool) {
    this.apiPool = apiPool;
    new AtomicLong();
  }

  @GET
  @ApiOperation(value = "", response = ProjectRoles.class)
  @Timed
  public ProjectRoles getRequest(@QueryParam("member") Optional<String> username, @Auth User user) {
	  return null;
  }

  @POST
  @ApiOperation(value = "", response = ProjectRoles.class)
  @Timed
  public ProjectRoleRequest postRequest(@Valid ProjectRoleRequest request, @Auth User user) {
	  
    ProjectRoleOperationPost operation = new ProjectRoleOperationPost();
    
    logger.debug("Received Request: ");
    logger.debug(request.getMember() + ":" + 
				request.getProject() + ":" +
    			request.getRole());

    operation.init(
            this.apiPool,  
            Optional.<String> absent(), 
            Optional.<Object> of(request), 
            user);    
    
    return (ProjectRoleRequest) (operation.perform());
  }
  
  @DELETE
  @ApiOperation(value = "", response = ProjectRoles.class)
  @Timed
  public ProjectRoleRequest deleteRequest(@Valid ProjectRoleRequest request, @Auth User user) {
    ProjectRoleOperationDelete operation = new ProjectRoleOperationDelete();

    operation.init(
            this.apiPool,  
            Optional.<String> absent(), 
            Optional.<Object> of(request), 
            user);
    
    return (ProjectRoleRequest) (operation.perform());
  }
  
}
