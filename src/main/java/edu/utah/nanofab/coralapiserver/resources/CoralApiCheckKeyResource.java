package edu.utah.nanofab.coralapiserver.resources;

import org.slf4j.Logger;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import edu.utah.nanofab.coralapi.CoralAPIInterface;
import edu.utah.nanofab.coralapi.CoralAPIPool;
import edu.utah.nanofab.coralapi.exceptions.CoralConnectionException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.security.Security;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@Path("/v0/checkKey")
@Api(value = "/v0/checkKey", description = "")

@Produces(MediaType.APPLICATION_JSON)
public class CoralApiCheckKeyResource {
  
  private CoralAPIPool apiPool;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiEnableResource.class);

  public CoralApiCheckKeyResource( CoralAPIPool apiPool ) {
      this.apiPool = apiPool;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "")  
  @Timed
  public Object checkKey() {
    CoralAPIInterface api = null;
    boolean keyValid = false;
    try {
        api = apiPool.getConnection("");
        keyValid = api.checkKeyIsValid();
    } catch (CoralConnectionException ex) {
        java.util.logging.Logger.getLogger(CoralApiCheckKeyResource.class.getName()).log(Level.SEVERE, null, ex);
    }
    HashMap returnValue = new HashMap();
    returnValue.put("message", "If key doesn't exist, check config.jar for certs/Coral.key\n"
                          + "If provider is not installed, see: http://www.randombugs.com/java/javalangsecurityexception-jce-authenticate-provider-bc.html");
    returnValue.put("keyExists", keyValid);
    returnValue.put("providerInstalled", checkProvider());
    return returnValue;
  }
  
  private boolean checkProvider() {
	  String Name= "BC";
      if (Security.getProvider(Name) == null)
      {
    	  return false;
      }
      else
      {
    	  return true;
      }	  
  }
    
}
