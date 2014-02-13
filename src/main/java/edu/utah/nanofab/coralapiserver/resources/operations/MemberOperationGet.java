package edu.utah.nanofab.coralapiserver.resources.operations;

public class MemberOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		this.setReturnValue(this.api.getMember(this.queryParam.get()));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get member with name: " + this.queryParam.get();
	}
	
}