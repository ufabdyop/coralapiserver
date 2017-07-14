package edu.utah.nanofab.coralapiserver.resources;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectMembershipOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectMembershipOperationPut;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectMembershipOperationDelete;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.utah.nanofab.coralapi.CoralAPIPool;


@Api(value = "/v0/projectMembership", description = "")
@Path("/v0/projectMembership")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiProjectMembershipResource {
     private CoralAPIPool apiPool;
     
  public CoralApiProjectMembershipResource(
      CoralAPIPool apiPool) {
      this.apiPool = apiPool;
  }

  @GET
  @ApiOperation(value = "", response = ProjectMembership.class)  
  @Timed
  public ProjectMembership get(@QueryParam("project") Optional<String> project, @Auth User user) {
    ProjectMembershipOperationGet operation = new ProjectMembershipOperationGet();
    operation.init( this.apiPool, project, Optional.<Object> absent(), user);
    return (ProjectMembership) (operation.perform());
  }
  
  @PUT
  @ApiOperation(value = "", response = ProjectMembership.class)  
  @Timed
  public ProjectMembership update(@Valid ProjectMembership request, @Auth User user) {
    ProjectMembershipOperationPut operation = new ProjectMembershipOperationPut();
    operation.init(
        this.apiPool,  
        Optional.<String> absent(), 
        Optional.<Object> of(request), 
        user);
    return (ProjectMembership) (operation.perform());
  }

  
  @DELETE
  @ApiOperation(value = "", response = ProjectMembership.class)  
  @Timed
  public ProjectMembership delete(@Valid ProjectMembership request, @Auth User user) {
    ProjectMembershipOperationDelete operation = new ProjectMembershipOperationDelete();
    operation.init(
        this.apiPool,  
        Optional.<String> absent(), 
        Optional.<Object> of(request), 
        user);
    return (ProjectMembership) (operation.perform());
  }

}
