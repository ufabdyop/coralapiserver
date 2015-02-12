package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.DisableRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.DisableOperationPost;

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

@Path("/v0/disable")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiDisableResource {
  
  private String coralIor;
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiEnableResource.class);

  public CoralApiDisableResource(String coralIor, String coralConfigUrl ) {
      this.coralIor = coralIor;
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @POST
  @Timed
  public DisableRequest createRequest(@Valid DisableRequest disableRequest, @Auth User user) {
    DisableOperationPost operation = new DisableOperationPost();
    operation.init(this.coralIor, 
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( disableRequest), 
        user);
    return (DisableRequest) (operation.perform());
  }
    
}
