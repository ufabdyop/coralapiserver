package edu.utah.nanofab.coralapiserver.resources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;

import edu.utah.nanofab.coralapiserver.auth.User;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "/v0/whoami", description = "")
@Path("/v0/whoami")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiWhoAmIResource {
  
  @GET
  @ApiOperation(value = "", response = User.class)
  @Timed
  public User getRequest(@Auth User user) {
	  return user;
  }  
  
}
