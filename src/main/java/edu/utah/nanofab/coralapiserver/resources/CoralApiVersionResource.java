package edu.utah.nanofab.coralapiserver.resources;

import com.codahale.metrics.annotation.Timed;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;


@Api(value = "/v0/version", description = "")
@Path("/v0/version")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiVersionResource {
    
    public CoralApiVersionResource() {

    }
    
    @GET
    @ApiOperation(value = "")    
    @Timed
    public String getRequest() throws IOException {
        return projectVersion();
    }
    
    private static String projectVersion() throws IOException {
      Properties p = new Properties();
      InputStream stream = null;
      
      stream = CoralApiVersionResource.class.getClassLoader().getResourceAsStream("app.properties");
      p.load(stream);

      stream.close();
      return "{ \"versionNumber\": \"" + p.getProperty("application.version") + "\" }";
    }
}
