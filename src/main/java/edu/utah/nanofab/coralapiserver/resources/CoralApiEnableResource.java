package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapiserver.auth.User;

import edu.utah.nanofab.coralapiserver.core.EnableRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.EnableOperationPost;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.utah.nanofab.coralapi.CoralAPIPool;
import java.util.HashMap;

import javax.ws.rs.core.Response;



@Path("/v0/enables")
@Api(value = "/v0/enables", description = "enable equipment, or fetch information about enables the equipment history")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiEnableResource {
  
  public static final Logger logger = LoggerFactory.getLogger(CoralApiEnableResource.class);
  private CoralAPIPool apiPool;

  public CoralApiEnableResource(CoralAPIPool apiPool ) {
      this.apiPool = apiPool;
      new AtomicLong();
  }

  @POST
  @ApiOperation(value = "", response = EnableRequest.class)  
  @Timed
  public HashMap<String, String> createRequest(@Valid EnableRequest enableRequest, @Auth User user) {
    EnableOperationPost operation = new EnableOperationPost();
    operation.init(
        this.apiPool, 
        Optional.<String> absent(), 
        Optional.<Object>of( enableRequest), 
        user);
      HashMap<String, String> result = (HashMap<String, String>) (operation.perform());
      return result;
        //return Response.status(200).entity(obj).build();
  }
    
}