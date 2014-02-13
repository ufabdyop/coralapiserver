package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.nanofab.coralapi.resource.Project;

public class ProjectOperationPost extends ResourceOperation  {
	@Override
	public void performOperationImpl() throws Exception {
		Project project = (Project)(this.postedObject.get());
		name = project.getName();
		this.api.CreateNewProject(project);
		this.setReturnValue(this.api.getProject(name));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get project with name: " + name;
	}

}
