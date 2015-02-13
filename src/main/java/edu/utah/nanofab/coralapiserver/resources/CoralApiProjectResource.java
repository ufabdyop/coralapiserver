package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.resource.Project;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationGet;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationPost;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectOperationPut;

import org.slf4j.Logger;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

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

@Path("/v0/project")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiProjectResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiProjectResource.class);

  public CoralApiProjectResource(String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @Timed
  public Project getRequest(@QueryParam("name") Optional<String> name, @Auth User user) {
    ProjectOperationGet operation = new ProjectOperationGet();
    operation.init( this.coralConfigUrl, name, Optional.<Object> absent(), user);
    return (Project) (operation.perform());
  }
 
  @POST
  @Timed
  public Project createRequest(@Valid Project project, @Auth User user) {
    ProjectOperationPost operation = new ProjectOperationPost();
    operation.init(
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( project), 
        user);
    return (Project) (operation.perform());
  }
  
  @PUT
  @Timed
  public Project updateRequest(@Valid Project project, @Auth User user) {
    ProjectOperationPut operation = new ProjectOperationPut();
    operation.init(
        this.coralConfigUrl, 
        Optional.<String> absent(), 
        Optional.<Object>of( project), 
        user);
    return (Project) (operation.perform());
  }

}
