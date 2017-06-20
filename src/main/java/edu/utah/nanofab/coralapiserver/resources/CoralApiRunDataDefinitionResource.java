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
import edu.utah.nanofab.coralapi.resource.RunDataProcess;
import edu.utah.nanofab.coralapiserver.resources.operations.RunDataDefinitionOperationGet;

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

@Path("/v0/rundataDefinitions")
@Api(description = "fetch rundata definitions", value = "/v0/rundataDefinitions")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiRunDataDefinitionResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiRunDataDefinitionResource.class);

  public CoralApiRunDataDefinitionResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "Find rundata definitions by tool name", response = RunDataProcess.class)  
  @Timed
  public RunDataProcess[] getRequest(@QueryParam("item") Optional<String> item, @Auth User user) {
    RunDataDefinitionOperationGet operation = new RunDataDefinitionOperationGet();
    operation.init( this.coralConfigUrl, item, Optional.<Object> absent(), user);
    return (RunDataProcess[]) (operation.perform());
  }

}
