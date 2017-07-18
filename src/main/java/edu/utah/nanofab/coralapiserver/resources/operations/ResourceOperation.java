package edu.utah.nanofab.coralapiserver.resources.operations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

import io.dropwizard.auth.Auth;
import edu.utah.nanofab.coralapi.CoralAPIInterface;
import edu.utah.nanofab.coralapi.CoralAPIPool;
import edu.utah.nanofab.coralapi.exceptions.CoralConnectionException;
import edu.utah.nanofab.coralapiserver.auth.User;
import edu.utah.nanofab.coralapiserver.core.ErrorResponse;
import java.util.logging.Level;
import org.opencoral.idl.NotAuthorizedSignal;

public abstract class ResourceOperation {
    public static String GlobalLock = "coral api not thread safe";
    protected CoralAPIPool apiPool;
    protected CoralAPIInterface api;
         
    public String error = null;
    public int statusCode = 500;
    protected User user;
    private Object returnValue = null;
    protected Optional<String> queryParam;
    protected Optional<Object> postedObject;
    protected String name = "";
    protected Logger logger;

    public void init(CoralAPIPool apiPool,
                    Optional<String> queryParam,
                    Optional<Object> postedObject,
                    @Auth User user) {

            logger = LoggerFactory.getLogger(ResourceOperation.class);
            this.apiPool = apiPool;
            this.queryParam = queryParam;
            this.postedObject = postedObject;
            this.user = user;
            logger.debug("ResourceOperation: " + this.queryParam + " " + this.user.getUsername());
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
	    try {
	      this.setUp();
	      this.performOperationImpl();
            } catch (NotAuthorizedSignal nas) {
                nas.printStackTrace();
                this.statusCode = 403;
                this.error = nas.reason;
                this.error += "\n\n" + nas.getMessage() + "\n";
	    } catch (Exception e) {
	      e.printStackTrace();
	      this.error = e.getMessage() + "\n";
	    }
	    this.reportErrorIfEncountered();
	    return returnValue();
        }
    }

    public void setReturnValue(Object object) {
            this.returnValue = object;
    }

    private void setUp() throws CoralConnectionException {
    	logger.debug("user: " + user.getUsername());
    	this.api = this.apiPool.getConnection(user.getUsername());
    }

    private Object returnValue() {
      return this.returnValue;
  }
    
    private void reportErrorIfEncountered() {
            String message = this.errorMessage();
            if (this.error != null) {
                ObjectMapper mapper = new ObjectMapper();
                ErrorResponse er = new ErrorResponse();
                er.message = message + " : "  + this.error;

                Response resp;
                try {
                    resp = Response.status(this.statusCode)
                            .entity(mapper.writeValueAsString(er)).build();
                } catch (JsonProcessingException ex) {
                    resp = Response.status(this.statusCode)
                            .entity(er.message).build();
                    java.util.logging.Logger.getLogger(ResourceOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
                throw new WebApplicationException(resp);
            }
    }
}
