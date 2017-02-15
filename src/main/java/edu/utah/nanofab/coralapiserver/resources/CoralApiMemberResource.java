package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapi.resource.Member;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.MemberName;
import edu.utah.nanofab.coralapiserver.core.ProjectName;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationPut;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.Authorization;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

@Path("/v0/members")
@Api(value = "/v0/members", description = "fetch or create/update members")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiMemberResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiMemberResource.class);

  public CoralApiMemberResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
  }

  @GET
  @ApiOperation(value = "Find member by name", 
					response = Member.class)    
  @Timed
  public Member getRequest(
		  @QueryParam("name") Optional<String> name, 
		  @Auth User user) {
    MemberOperationGet operation = new MemberOperationGet();
    operation.init( this.coralConfigUrl, name, Optional.<Object> absent(), user);
    Member tempMember = (Member) (operation.perform());
    this.logger.debug("fetched member: ");
    this.logger.debug(tempMember.toString());
    return tempMember;
  }
    
  @PUT
  @ApiOperation(value = "Update member", 
	response = Member.class)    
  @Timed
  public Member updateRequest(@Valid Member member, @Auth User user) {
    MemberOperationPut operation = new MemberOperationPut();
    operation.init(
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( member), 
        user);
    return (Member) (operation.perform());
  }
  
  @POST
  @ApiOperation(value = "Create member", 
	response = Member.class)    
  @Timed
  public Member createRequest(@Valid Member member, @Auth User user) {
    MemberOperationPost operation = new MemberOperationPost();
    operation.init( 
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( member), 
        user);
    return (Member) (operation.perform());
  }
  
  @POST
  @ApiOperation(value = "Activate Member", response = MemberName.class)
  @Path("/activate")
  @Timed
  public MemberName activate(@Valid MemberName member, @Auth User user) throws Exception {
	  	CoralAPI api = new CoralAPI(user.getUsername(), this.coralConfigUrl);
		try {
			api.activateMember(member.getMember());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Caught exception activating " + member.getMember());
			throw e;
		}
		return member;
  }  

  @POST
  @ApiOperation(value = "Activate Member With Project", response = MemberName.class)
  @Path("/activateWithProject")
  @Timed
  public ProjectMembership activateWithProject(@Valid ProjectMembership memberProject, @Auth User user) throws Exception {
        CoralAPI api = new CoralAPI(user.getUsername(), this.coralConfigUrl);
        String member = null;
        String project = null;
        try {
            member = memberProject.getMembers()[0];
            project = memberProject.getProject();
            api.activateMemberWithProject(
                        member, 
                        project);
        } catch (Exception e) {
                e.printStackTrace();
                logger.error("Caught exception activating with project" + member);
                throw e;
        }
        return memberProject;
  }
  
  @POST
  @ApiOperation(value = "Deactivate Member", response = MemberName.class)
  @Path("/deactivate")
  @Timed
  public MemberName deactivate(@Valid MemberName member, @Auth User user) throws Exception {
	  	CoralAPI api = new CoralAPI(user.getUsername(), this.coralConfigUrl);
		try {
			api.deactivateMember(member.getMember());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Caught exception deactivating " + member.getMember());
			throw e;
		}
		return member;
  }  

}
