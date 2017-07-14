package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.DisableRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.DisableOperationPost;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;
import edu.utah.nanofab.coralapi.CoralAPIPool;
import edu.utah.nanofab.coralapiserver.core.DisableWithRundataRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.DisableWithRundataOperationPost;
import java.util.HashMap;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Path("/v0/disables")
@Api(value = "/v0/disables", description = "Disable machines")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiDisableResource {
  
  public static final Logger logger = LoggerFactory.getLogger(CoralApiEnableResource.class);
  private CoralAPIPool apiPool;

  public CoralApiDisableResource(CoralAPIPool apiPool) {
      this.apiPool = apiPool;
      
      new AtomicLong();
  }

  @POST
  @ApiOperation(value = "", response = DisableRequest.class)  
  @Timed
  public HashMap<String, String> createRequest(@Valid DisableRequest disableRequest, @Auth User user) {
    DisableOperationPost operation = new DisableOperationPost();
    operation.init(
        this.apiPool, 
        Optional.<String> absent(), 
        Optional.<Object>of( disableRequest), 
        user);
    HashMap<String, String> result = (HashMap<String, String>) (operation.perform());
    return result;
  }
  
  @POST
  @Path("/withRundata")
  @ApiOperation(value = "", response = DisableRequest.class)  
  @Timed
  public HashMap<String, String> disableWithRundataRequest(@Valid DisableWithRundataRequest disableRequest, @Auth User user) {
    DisableWithRundataOperationPost operation = new DisableWithRundataOperationPost();
    operation.init(
        this.apiPool, 
        Optional.<String> absent(), 
        Optional.<Object>of( disableRequest), 
        user);
    HashMap<String, String> result = (HashMap<String, String>) (operation.perform());
    return result;
  }
}