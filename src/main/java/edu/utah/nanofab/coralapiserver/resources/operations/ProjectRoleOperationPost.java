package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.ProjectRoleRequest;

public class ProjectRoleOperationPost extends ResourceOperation  {
	  @Override
	  public void performOperationImpl() throws Exception {
		  ProjectRoleRequest request = (ProjectRoleRequest) this.postedObject.get();
		  this.api.addProjectRoleToMember(
				  request.getMember(), 
				  request.getRole(), 
				  request.getProject()
				  );
		  
		  this.setReturnValue(request);
	  }

	  @Override
	  public String errorMessage() {
	    return "Error while trying to post to project role " ;
	  }
	  
}
