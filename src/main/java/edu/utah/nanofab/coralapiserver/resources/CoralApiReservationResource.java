package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapi.collections.Reservations;
import edu.utah.nanofab.coralapi.resource.Project;
import edu.utah.nanofab.coralapi.resource.Reservation;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ReservationRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.EnableOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.ReservationOperationDelete;
import edu.utah.nanofab.coralapiserver.resources.operations.ReservationOperationPost;

import org.slf4j.Logger;

import com.google.common.base.Optional;

import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Api(value = "/v0/reservation", description = "")
@Path("/v0/reservation")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiReservationResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiReservationResource.class);

  public CoralApiReservationResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @POST
  @ApiOperation(value = "", response = ReservationRequest.class)  
  @Timed
  public ReservationRequest createRequest(@Valid ReservationRequest request, @Auth User user) {
    ReservationOperationPost operation = new ReservationOperationPost();
    operation.init(
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( request ), 
        user);
    return (ReservationRequest) (operation.perform());
  }
  
  @GET
  @ApiOperation(value = "", response = ReservationRequest.class)    
  @Timed
  public Reservations getRequest(@QueryParam("machine") Optional<String> machine, @Auth User user) throws Exception {
	  CoralAPI coralApiInstance = new CoralAPI(user.getUsername(), this.coralConfigUrl);
	  Date bdate = new Date();
	  Date edate = new Date();
	  long edateMS = bdate.getTime();
	  edateMS += (30L * 24L * 60L * 60L * 1000L);
	  edate.setTime(edateMS); //30 days in the future;
	  return coralApiInstance.getReservations(machine.get(), bdate, edate);
  }

  @DELETE
  @ApiOperation(value = "", response = ReservationRequest.class)    
  @Timed
  public ReservationRequest deleteRequest(@Valid ReservationRequest request, @Auth User user) {
    ReservationOperationDelete operation = new ReservationOperationDelete();
    operation.init(
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( request ), 
        user);
    return (ReservationRequest) (operation.perform());
  }
      
}
