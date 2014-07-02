package edu.utah.nanofab.coralapiserver.resources.operations;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.google.common.base.Optional;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.yammer.dropwizard.auth.Auth;

import edu.utah.nanofab.coralapi.CoralAPI;
import edu.utah.nanofab.coralapiserver.auth.User;

public abstract class ResourceOperation {
    public String coralIor;
    public String coralConfigUrl;
    public CoralAPI api;
    public String error = null;
    protected User user;
    private Object returnValue = null;
    protected Optional<String> queryParam;
    protected Optional<Object> postedObject;
    protected String name = "";

    public void init(String coralIor, String coralConfigUrl,
                    Optional<String> queryParam,
                    Optional<Object> postedObject,
                    @Auth User user) {
            this.coralConfigUrl = coralConfigUrl;
            this.coralIor = coralIor;
            this.queryParam = queryParam;
            this.postedObject = postedObject;
            this.user = user;
    }

    /**
     * This function needs to use the api to perform some operation and set
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
		this.setUp();
		try {
			this.performOperationImpl();
		} catch (Exception e) {
			this.error = e.getMessage();
		}
		this.tearDown();
		this.reportErrorIfEncountered();
		return returnValue();
    }

    public void setReturnValue(Object object) {
            this.returnValue = object;
    }

    private void setUp() {
    this.api = new CoralAPI(this.user.getUsername(),
                            this.coralIor, this.coralConfigUrl);
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
