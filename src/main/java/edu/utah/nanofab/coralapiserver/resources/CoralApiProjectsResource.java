package edu.utah.nanofab.coralapiserver.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

import edu.utah.nanofab.coralapi.collections.Projects;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.resources.operations.ProjectsOperationGet;

@Path("/projects")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiProjectsResource {
  
  private String coralIor;
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiProjectsResource.class);
  
  public CoralApiProjectsResource(String coralIor, String coralConfigUrl) {
    this.coralIor = coralIor;
    this.coralConfigUrl = coralConfigUrl;
  }

  @GET
  @Timed
  public Projects getRequest(@QueryParam("member") Optional<String> member, @Auth User user) {
    ProjectsOperationGet operation = new ProjectsOperationGet();
    operation.init(this.coralIor, this.coralConfigUrl, member, Optional.<Object> absent(), user);
    return (Projects) (operation.perform());
  }
}
