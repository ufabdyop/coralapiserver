package edu.utah.nanofab.coralapiserver.resources.operations;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;

import io.dropwizard.auth.Auth;
import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapiserver.auth.User;

public abstract class ResourceOperation {
    public static String GlobalLock = "coral api not thread safe";
    public String coralConfigUrl;
    public CoralAPI api;
    public String error = null;
    protected User user;
    private Object returnValue = null;
    protected Optional<String> queryParam;
    protected Optional<Object> postedObject;
    protected String name = "";
    protected Logger logger;

    public void init(String coralConfigUrl,
                    Optional<String> queryParam,
                    Optional<Object> postedObject,
                    @Auth User user) {

    		logger = LoggerFactory.getLogger(ResourceOperation.class);
            this.coralConfigUrl = coralConfigUrl;
            this.queryParam = queryParam;
            this.postedObject = postedObject;
            this.user = user;
            logger.debug("ResourceOperation: " + this.coralConfigUrl + " " + this.queryParam + " " + this.user.getUsername());
    }

    /**
     * This function needs to use the Coral API to perform some operation and set
     * the returnValue object.  If there is an error, it must set the error
     * member field to a descriptive string.
     * @throws Exception 
     */
    public abstract void performOperationImpl() throws Exception;

    /**
     * This is a generic error message that describes what the operation
     * was meant to do.
     * @return
     */
    public abstract String errorMessage();
  
    public Object perform() {
        synchronized(GlobalLock) {
	    this.setUp();
	    try {
	      this.performOperationImpl();
	    } catch (Exception e) {
	      e.printStackTrace();
	      this.error = e.getMessage();
	    }
	    this.tearDown();
	    this.reportErrorIfEncountered();
	    return returnValue();
        }
    }

    public void setReturnValue(Object object) {
            this.returnValue = object;
    }

    private void setUp() {
    	logger.debug("user: " + user.getUsername());
    	logger.debug("configUrl: " + coralConfigUrl);
    	this.api = new CoralAPI(this.user.getUsername(),
                            this.coralConfigUrl);
    }

    private void tearDown() {
    this.api.close();
    this.api = null;
    }
  
    private Object returnValue() {
      return this.returnValue;
  }
    
    private void reportErrorIfEncountered() {
            String message = this.errorMessage();
            if (this.error != null) {
                Response resp = new ResponseBuilderImpl().status(500)
                .entity(message + "\": "  + this.error).build();
                    throw new WebApplicationException(resp);
            }
    }
}
