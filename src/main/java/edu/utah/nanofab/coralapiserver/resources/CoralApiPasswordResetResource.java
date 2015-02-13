package edu.utah.nanofab.coralapiserver.resources;


import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import io.dropwizard.auth.Auth;
import com.codahale.metrics.annotation.Timed;

import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.PasswordResetRequest;
import edu.utah.nanofab.coralapiserver.resources.operations.PasswordResetOperationPost;

@Path("/v0/resetPassword")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiPasswordResetResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiPasswordResetResource.class);
  
  /**
   * Constructs a new password reset resource with the supplied coralConfigUrl.
   * 
   * @param coralConfigUrl The URL endpoint for the CORBA configurations.
   */
  public CoralApiPasswordResetResource(String coralConfigUrl) {
    this.coralConfigUrl = coralConfigUrl;
  }
  
  /**
   * Updates a coral member's password with the new password that is supplied.
   * 
   * @param name The user name of the coral member who's password will be updated.
   * @param newPass The new password for the coral account.
   * @param user The authenticated user.
   * 
   * @return True if the member's password was successfully updated. False otherwise.
   */
  @POST
  @Timed
  public Object updatePassword(@Valid PasswordResetRequest request, @Auth User user) {
    PasswordResetOperationPost operation = new PasswordResetOperationPost();
    
    if (request.getName().equals("auth-token")) {
    	request.setName(user.getUsername());
    }
    
    operation.init( 
             this.coralConfigUrl, 
             Optional.<String> absent(), 
             Optional.<Object> of(request), 
             user
    );
    
    return operation.perform();
  }
}
