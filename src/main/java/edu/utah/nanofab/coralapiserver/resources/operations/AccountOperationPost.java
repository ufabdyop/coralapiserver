package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.resource.Account;

public class AccountOperationPost extends ResourceOperation  {

	@Override
	public void performOperationImpl() throws Exception {
		Account account = (Account)(this.postedObject.get());
		name = account.getName();
		this.api.createNewAccount(account);
		this.setReturnValue(this.api.getAccount(name));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get account with name: " + this.name;
	}

}
