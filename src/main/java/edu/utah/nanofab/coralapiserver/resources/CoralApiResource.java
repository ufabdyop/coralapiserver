package edu.utah.nanofab.coralapiserver.resources;

import edu.utah.nanofab.coralapiserver.TokenConfiguration;
import edu.utah.nanofab.coralapiserver.core.Saying;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import java.util.concurrent.atomic.AtomicLong;

@Path("/coral-api")
@Produces(MediaType.APPLICATION_JSON)
public class CoralApiResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
	private final TokenConfiguration[] tokens;

    public CoralApiResource(String template, String defaultName, TokenConfiguration[] tokens) {
        this.template = template;
        this.defaultName = defaultName;
        this.tokens = tokens;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public Saying sayHello(@QueryParam("name") Optional<String> name) {
    	String buffer = "";
    	for(int i = 0; i < tokens.length; i++) {
    		buffer += tokens[i].getToken() + " : " + tokens[i].getUser() + "\n";
    	}
        return new Saying(counter.incrementAndGet(),
                          String.format(template, name.or(defaultName) + "tokens: " + buffer));
    }
}
