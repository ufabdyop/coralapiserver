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
import edu.utah.nanofab.coralapi.CoralAPISynchronized;
import edu.utah.nanofab.coralapi.exceptions.RequestFailedException;
import edu.utah.nanofab.coralapi.resource.RunDataProcess;
import edu.utah.nanofab.coralapiserver.core.GenericResponse;
import edu.utah.nanofab.coralapiserver.core.RunDataEntry;
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
import java.util.logging.Level;
import javax.ws.rs.core.Response;
import org.opencoral.idl.Runtime.NullReturnException;
import org.opencoral.idl.Runtime.ServerErrorException;

@Path("/v0/rundata")
@Api(description = "fetch and create rundata", value = "/v0/rundata")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiRunDataResource {
  
  public static final Logger logger = LoggerFactory.getLogger(CoralApiRunDataResource.class);
  private final CoralAPIPool apiPool;

  public CoralApiRunDataResource(CoralAPIPool apiPool ) {
      this.apiPool = apiPool;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "Find rundata entries", response = RunDataProcess.class)  
  @Timed
  public String getRequest(@Auth User user) {
    return ("TBD");
  }

  @POST
  @ApiOperation(value = "Create rundata entry", response = RunDataEntry.class)  
  @Timed
  public Response postRequest(@Valid RunDataEntry rundata, @Auth User user) {
      CoralAPISynchronized coralApiInstance = apiPool.getConnection(user.getUsername());
      GenericResponse r = new GenericResponse(true, "");
          
      try {
          coralApiInstance.createRunData(rundata.xmlDefinition);
          r.setMessage("Created rundata");
      } catch (NullReturnException ex) {
          r.setSuccess(false);
          r.setMessage("NullReturnException while saving rundata");
      } catch (ServerErrorException ex) {
          r.setSuccess(false);
          r.setMessage("ServerErrorException while saving rundata");
      } catch (Exception ex) {
          r.setSuccess(false);
          r.setMessage("Exception while saving rundata");
      }
          
      if (r.isSuccess()) {
        return Response.status(Response.Status.OK).entity(r).build();      
      } else {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(r).build();      
      }
  }

}
