package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.collections.Machines;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.MachineOperationGet;

import org.slf4j.Logger;

import com.google.common.base.Optional;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Api(value = "/v0/machines", description = "")
@Path("/v0/machines")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiMachineResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiMachineResource.class);

  public CoralApiMachineResource( String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "", response = Machines.class)  
  @Timed
  public Object getRequest(@QueryParam("name") Optional<String> name, @Auth User user) {
    MachineOperationGet operation = new MachineOperationGet();
    operation.init(this.coralConfigUrl, name, Optional.<Object> absent(), user);
    return operation.perform();
  }
  
}
