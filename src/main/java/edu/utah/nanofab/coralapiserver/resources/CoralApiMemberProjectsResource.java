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
import edu.utah.nanofab.coralapiserver.core.MemberProjects;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberProjectsOperationDelete;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberProjectsOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberProjectsOperationPut;
import java.util.ArrayList;
import java.util.HashMap;


@Api(value = "/v0/memberProjects", description = "")
@Path("/v0/memberProjects")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiMemberProjectsResource {
  private CoralAPIPool apiPool;
  
  public CoralApiMemberProjectsResource(
      CoralAPIPool apiPool) {
      this.apiPool = apiPool;
  }


  @GET
  @ApiOperation(value = "", response = MemberProjects.class)  
  @Timed
  public HashMap<String, ArrayList<String>> get(
          @QueryParam("member") Optional<String> member, 
          @Auth User user) {
    MemberProjectsOperationGet operation = new MemberProjectsOperationGet();
    operation.init(apiPool, member, Optional.<Object> absent(), user);
    HashMap<String, ArrayList<String>> returnSet;
    
    returnSet = (HashMap<String, ArrayList<String>>) (operation.perform());
    return returnSet;
  }
  
  @PUT
  @ApiOperation(value = "", response = MemberProjects.class)  
  @Timed
  public MemberProjects update(@Valid MemberProjects request, @Auth User user) {
    MemberProjectsOperationPut operation = new MemberProjectsOperationPut();
    operation.init(
        this.apiPool,  
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
        this.apiPool,  
        Optional.<String> absent(), 
        Optional.<Object> of(request), 
        user);
    return (MemberProjects) (operation.perform());
  }

}
