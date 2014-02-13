package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.nanofab.coralapi.CoralServices;
import edu.nanofab.coralapi.collections.Members;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;

public class ProjectMembershipOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		String project = this.queryParam.get();
		Members members = this.api.GetProjectMembers(project);
		this.setReturnValue(new ProjectMembership(project, members));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get memberships for project with name: " + this.queryParam.get();
	}
	
}