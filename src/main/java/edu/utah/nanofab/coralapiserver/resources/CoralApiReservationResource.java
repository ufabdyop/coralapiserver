package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapi.collections.Reservations;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ReservationRequest;

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
import edu.utah.nanofab.coralapi.CoralAPIPool;
import edu.utah.nanofab.coralapi.CoralAPISynchronized;


@Api(value = "/v0/reservations", description = "Create reservations or fetch reservations")
@Path("/v0/reservations")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiReservationResource {
  
  public static final Logger logger = LoggerFactory.getLogger(CoralApiReservationResource.class);
  private final CoralAPIPool apiPool;

  public CoralApiReservationResource(CoralAPIPool apiPool ) {
      this.apiPool = apiPool;
      new AtomicLong();
  }

  @POST
  @ApiOperation(value = "", response = ReservationRequest.class)  
  @Timed
  public ReservationRequest createRequest(@Valid ReservationRequest request, 
          @Auth User user) throws Exception {
      
      System.out.println("Reservation creation for " + request.getItem());
      CoralAPISynchronized coralApiInstance = apiPool.getConnection(user.getUsername());
              
      coralApiInstance.createNewReservation(user.getUsername(), 
              request.getMember(), 
              request.getProject(), 
              request.getItem(), 
              request.getBdate(), 
              request.getLengthInMinutes());
      
      return request;
  }
  
  @GET
  @ApiOperation(value = "", response = ReservationRequest.class)    
  @Timed
  public Reservations getRequest(@QueryParam("machine") Optional<String> machine, @Auth User user) throws Exception {
        CoralAPISynchronized coralApiInstance = apiPool.getConnection(user.getUsername());
        
        Date bdate = new Date();
        Date edate = new Date();
        long edateMS = bdate.getTime();
        edateMS += (30L * 24L * 60L * 60L * 1000L);
        edate.setTime(edateMS); //30 days in the future;
        Reservations r = coralApiInstance.getReservations(machine.get(), bdate, edate);
        
        return r;
  }

  @DELETE
  @ApiOperation(value = "", response = ReservationRequest.class)    
  @Timed
  public ReservationRequest deleteRequest(@Valid ReservationRequest request, @Auth User user) throws Exception {
    logger.debug("Got DELETE RESERVATION request : " + request.getItem() + " " + request.getBdate());
    CoralAPISynchronized coralApiInstance = apiPool.getConnection(user.getUsername());
    
    System.out.println("Reservation deletion for " + request.getItem() + " " + request.getBdate());
    coralApiInstance.deleteReservation(
            user.getUsername(), 
            request.getMember(), 
            request.getProject(), 
            request.getItem(), 
            request.getBdate(), 
            request.getLengthInMinutes()
    );

    return request;
  }
      
}
