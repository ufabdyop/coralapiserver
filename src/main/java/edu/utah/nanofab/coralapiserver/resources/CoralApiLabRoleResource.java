package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.collections.LabRoles;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.LabRoleOperationGet;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

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
import edu.utah.nanofab.coralapiserver.core.GenericRoleRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.LabRoleOperationDelete;
import edu.utah.nanofab.coralapiserver.resources.operations.LabRoleOperationPost;
import javax.validation.Valid;

@Path("/v0/labRoles")
@Api(value = "/v0/labRoles", description = "")

@Produces(MediaType.APPLICATION_JSON)
public class CoralApiLabRoleResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiLabRoleResource.class);

  public CoralApiLabRoleResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "", response = LabRoles.class)
  @Timed
  public LabRoles getRequest(@QueryParam("member") Optional<String> username, @Auth User user) {
    LabRoleOperationGet operation = new LabRoleOperationGet();
    operation.init(this.coralConfigUrl, username, Optional.<Object> absent(), user);
    return (LabRoles) (operation.perform());
  }

  @POST
  @ApiOperation(value = "", response = LabRoles.class)
  @Timed
  public GenericRoleRequest postRequest(@Valid GenericRoleRequest request, @Auth User user) {
	  
    LabRoleOperationPost operation = new LabRoleOperationPost();
    
    logger.debug("Received Request: ");
    logger.debug(request.getMember() + ":" + 
				request.getTarget() + ":" +
    			request.getRole());

    operation.init(
            this.coralConfigUrl,  
            Optional.<String> absent(), 
            Optional.<Object> of(request), 
            user);    
    
    return (GenericRoleRequest) (operation.perform());
  }
  
  @DELETE
  @ApiOperation(value = "", response = LabRoles.class)
  @Timed
  public GenericRoleRequest deleteRequest(@Valid GenericRoleRequest request, @Auth User user) {
    LabRoleOperationDelete operation = new LabRoleOperationDelete();

    operation.init(
            this.coralConfigUrl,  
            Optional.<String> absent(), 
            Optional.<Object> of(request), 
            user);
    
    return (GenericRoleRequest) (operation.perform());
  }  
  
}
