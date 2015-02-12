package edu.utah.nanofab.coralapiserver.resources;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import io.dropwizard.auth.Auth;

import com.codahale.metrics.annotation.Timed;

import edu.utah.nanofab.coralapiserver.auth.User;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class CoralApiEntryPointResource {
  
  @GET
  @Timed
  public String getRequest() {
	  String reference = "";
	  reference += "<!DOCTYPE html> \n";
	  reference += "<html>\n";
	  reference += "<head>\n";
	  reference += "<title>See Docs</title>\n";
	  reference += "</head>\n";
  	  reference += "<body>\n";
	  reference += "For documentation, please see <a href=\"/assets/docs/index.html\">/assets/docs/index.html</a>\n";
	  reference += "</body>\n";
	  reference += "</html>\n";
	  return reference;
  }  
  
}
