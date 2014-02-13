package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.nanofab.coralapi.resource.Account;

public class AccountOperationPut extends ResourceOperation  {
	@Override
	public void performOperationImpl() throws Exception {
		Account account = (Account)(this.postedObject.get());
		name = account.getName();
		this.api.updateAccount(account);
		this.setReturnValue(this.api.getAccount(name));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get account with name: " + name;
	}

}
