package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.collections.LabRoles;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.LabRoleOperationGet;

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

@Path("/labRoles")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiLabRoleResource {
  
  private String coralIor;
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiLabRoleResource.class);

  public CoralApiLabRoleResource(String coralIor, String coralConfigUrl ) {
      this.coralIor = coralIor;
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @Timed
  public LabRoles getRequest(@QueryParam("member") Optional<String> username, @Auth User user) {
    LabRoleOperationGet operation = new LabRoleOperationGet();
    operation.init(this.coralIor, this.coralConfigUrl, username, Optional.<Object> absent(), user);
    return (LabRoles) (operation.perform());
  }
}
