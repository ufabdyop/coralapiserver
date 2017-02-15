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
import edu.utah.nanofab.coralapi.collections.EquipmentRoles;
import edu.utah.nanofab.coralapiserver.core.GenericRoleRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.EquipmentRoleOperationDelete;
import edu.utah.nanofab.coralapiserver.resources.operations.EquipmentRoleOperationPost;

@Path("/v0/equipmentRoles")
@Api(value = "/v0/equipmentRoles", description = "")

@Produces(MediaType.APPLICATION_JSON)
public class CoralApiEquipmentRoleResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiEquipmentRoleResource.class);

  public CoralApiEquipmentRoleResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "", response = ProjectRoles.class)
  @Timed
  public EquipmentRoles getRequest(@QueryParam("member") Optional<String> username, @Auth User user) {
	  return null;
  }

  @POST
  @ApiOperation(value = "", response = ProjectRoles.class)
  @Timed
  public GenericRoleRequest postRequest(@Valid GenericRoleRequest request, @Auth User user) {
	  
    EquipmentRoleOperationPost operation = new EquipmentRoleOperationPost();
    
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
  @ApiOperation(value = "", response = ProjectRoles.class)
  @Timed
  public GenericRoleRequest deleteRequest(@Valid GenericRoleRequest request, @Auth User user) {
    EquipmentRoleOperationDelete operation = new EquipmentRoleOperationDelete();

    operation.init(
            this.coralConfigUrl,  
            Optional.<String> absent(), 
            Optional.<Object> of(request), 
            user);
    
    return (GenericRoleRequest) (operation.perform());
  }
  
}
