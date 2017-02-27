package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.resource.LabRole;
import edu.utah.nanofab.coralapiserver.core.GenericRoleRequest;

public class LabRoleOperationPost extends ResourceOperation  {
	  @Override
	  public void performOperationImpl() throws Exception {
		  GenericRoleRequest req = (GenericRoleRequest) this.postedObject.get();
		  this.api.addLabRoleToMember(new LabRole(req.getTarget(), 
                          req.getMember(), 
                          req.getRole()));
		  
		  this.setReturnValue(req);
	  }

	  @Override
	  public String errorMessage() {
	    return "Error while trying to post to project role " ;
	  }
	  
}
