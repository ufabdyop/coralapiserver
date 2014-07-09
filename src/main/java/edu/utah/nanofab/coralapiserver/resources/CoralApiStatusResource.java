package edu.utah.nanofab.coralapiserver.resources;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiStatusResource {
    
    public static final Logger logger = LoggerFactory.getLogger(CoralApiMemberResource.class);
    
    public CoralApiStatusResource(String coralIor, String coralConfigUrl ) {
        new AtomicLong();
    }
    
    
}
