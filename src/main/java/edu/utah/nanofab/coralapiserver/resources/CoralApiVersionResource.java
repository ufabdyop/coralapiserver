package edu.utah.nanofab.coralapiserver.resources;

import com.google.common.io.Closeables;
import com.yammer.metrics.annotation.Timed;
import edu.utah.nanofab.coralapi.resource.Project;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiVersionResource {
    
    public CoralApiVersionResource() {

    }
    
    @GET
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
        return p.getProperty("application.version");
    }
}
