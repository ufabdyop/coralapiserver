package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapi.CoralAPI;

import org.slf4j.Logger;

import com.codahale.metrics.annotation.Timed;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.LoggerFactory;

import java.security.Security;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@Path("/v0/checkKey")
@Api(value = "/v0/checkKey", description = "")

@Produces(MediaType.APPLICATION_JSON)
public class CoralApiCheckKeyResource {
  
  private String coralConfigUrl;
  public static final Logger logger = LoggerFactory.getLogger(CoralApiEnableResource.class);

  public CoralApiCheckKeyResource( String coralConfigUrl ) {
      this.coralConfigUrl = coralConfigUrl;
      new AtomicLong();
  }

  @GET
  @ApiOperation(value = "")  
  @Timed
  public Object checkKey() {
	  CoralAPI api = new CoralAPI("", coralConfigUrl);
	  boolean keyValid = api.checkKeyIsValid();
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
