package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ReservationRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.EnableOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.ReservationOperationPost;

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
    
}
