package edu.utah.nanofab.coralapiserver.resources.operations;

public class AccountOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		this.setReturnValue(this.api.getAccount(this.queryParam.get()));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get account with name: " + this.queryParam.get();
	}
	
}