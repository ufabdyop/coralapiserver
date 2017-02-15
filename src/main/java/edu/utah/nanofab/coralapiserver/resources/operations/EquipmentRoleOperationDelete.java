package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.GenericRoleRequest;

public class EquipmentRoleOperationDelete extends ResourceOperation  {
	  @Override
	  public void performOperationImpl() throws Exception {
		  GenericRoleRequest request = (GenericRoleRequest) this.postedObject.get();
		  this.api.removeEquipmentRoleFromMember(
				  request.getMember(), 
				  request.getRole(), 
				  request.getTarget()
				  );
		  
		  this.setReturnValue(request);
	  }

	  @Override
	  public String errorMessage() {
	    return "Error while trying to delete project role ";
	  }
	  
}
