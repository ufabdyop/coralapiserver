package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.resource.Account;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.AccountOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.AccountOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.AccountOperationPut;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;
import edu.utah.nanofab.coralapi.CoralAPIPool;

import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

@Path("/v0/clearApiPool")
@Api(description = "Clear the api pool of stale connections", value = "/v0/clearApiPool")
@Produces(MediaType.APPLICATION_JSON)
public class ClearApiPool {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiAccountResource.class);
  private final CoralAPIPool apiPool;

  public ClearApiPool(CoralAPIPool apiPool ) {
      this.apiPool = apiPool;
      new AtomicLong();
  }

  @POST
  @ApiOperation(value = "Clear the api pool of stale connections", response = String.class)
  @Timed
  public String createRequest() {
      this.apiPool.closeConnectionsOlderThan(3600);
      return "{\"message\": \"cleared\"}";
  }
  
}
