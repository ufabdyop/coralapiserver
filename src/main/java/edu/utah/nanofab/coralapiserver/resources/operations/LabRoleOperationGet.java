package edu.utah.nanofab.coralapiserver.resources.operations;

public class LabRoleOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		this.setReturnValue(this.api.getLabRoles(this.queryParam.get()));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get member with name: " + this.queryParam.get();
	}
	
}