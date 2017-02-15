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
import edu.utah.nanofab.coralapiserver.core.MemberProjects;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberProjectsOperationDelete;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberProjectsOperationPut;


@Api(value = "/v0/memberProjects", description = "")
@Path("/v0/memberProjects")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiMemberProjectsResource {
  private String coralConfigUrl;

  public CoralApiMemberProjectsResource(
      String coralConfigUrl) {
    this.coralConfigUrl = coralConfigUrl;
  }

  @PUT
  @ApiOperation(value = "", response = MemberProjects.class)  
  @Timed
  public MemberProjects update(@Valid MemberProjects request, @Auth User user) {
    MemberProjectsOperationPut operation = new MemberProjectsOperationPut();
    operation.init(
        this.coralConfigUrl,  
        Optional.<String> absent(), 
        Optional.<Object> of(request), 
        user);
    return (MemberProjects) (operation.perform());
  }
  
  @DELETE
  @ApiOperation(value = "", response = MemberProjects.class)  
  @Timed
  public MemberProjects delete(@Valid MemberProjects request, @Auth User user) {
    MemberProjectsOperationDelete operation = new MemberProjectsOperationDelete();
    operation.init(
        this.coralConfigUrl,  
        Optional.<String> absent(), 
        Optional.<Object> of(request), 
        user);
    return (MemberProjects) (operation.perform());
  }

}
