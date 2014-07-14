package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.PasswordResetRequest;


public class PasswordResetOperationPost extends ResourceOperation {
 
	@Override
	public void performOperationImpl() throws Exception {
		PasswordResetRequest request = (PasswordResetRequest) this.postedObject.get();
		String memberName = request.getName();
		String newPass = request.getPassword();
		
		try {
			this.api.updatePassword(memberName, newPass);
		} catch(Exception e) {
			this.setReturnValue(false);
			return;
		}
		
		this.setReturnValue(true);
		return;
	}

	@Override
	public String errorMessage() {
		return "Error: Could not update the password for member '" + name + "'";
	}

}
