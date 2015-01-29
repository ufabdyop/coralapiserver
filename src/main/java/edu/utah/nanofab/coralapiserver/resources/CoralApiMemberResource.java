package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.resource.Member;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.MemberOperationPut;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

@Path("/member")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiMemberResource {
  
  private String coralIor;
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiMemberResource.class);

  public CoralApiMemberResource(String coralIor, String coralConfigUrl ) {
      this.coralIor = coralIor;
      this.coralConfigUrl = coralConfigUrl;
  }

  @GET
  @Timed
  public Member getRequest(@QueryParam("name") Optional<String> name, @Auth User user) {
    MemberOperationGet operation = new MemberOperationGet();
    operation.init(this.coralIor, this.coralConfigUrl, name, Optional.<Object> absent(), user);
    return (Member) (operation.perform());
  }
    
  @POST
  @Timed
  public Member updateRequest(@Valid Member member, @Auth User user) {
    MemberOperationPost operation = new MemberOperationPost();
    operation.init(this.coralIor, 
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( member), 
        user);
    return (Member) (operation.perform());
  }
  
  @PUT
  @Timed
  public Member createRequest(@Valid Member member, @Auth User user) {
    MemberOperationPut operation = new MemberOperationPut();
    operation.init(this.coralIor, 
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( member), 
        user);
    return (Member) (operation.perform());
  }

}
