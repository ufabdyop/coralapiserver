package edu.utah.nanofab.coralapiserver.resources;


import java.io.IOException;
import java.io.InputStream;

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
	  String html;
      try {
    	  InputStream stream = CoralApiEntryPointResource.class.getClassLoader().getResourceAsStream("index.html");
    	  html = convertStreamToString(stream);
    	  stream.close();
      } catch (Exception e) {
    	  e.printStackTrace();
    	  System.err.println("Failing to load index.html, falling back to inline html");
    	  html = getStaticHtml();
      }
      return html;
  }
  
  private String getStaticHtml() {
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
  
  //http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
  private String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
  
}
