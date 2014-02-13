package edu.utah.nanofab.coralapiserver.resources.operations;

public class ProjectOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		this.setReturnValue(this.api.getProject(this.queryParam.get()));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get project with name: " + this.queryParam.get();
	}
	
}