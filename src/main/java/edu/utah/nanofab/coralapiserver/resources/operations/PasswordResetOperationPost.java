package edu.utah.nanofab.coralapiserver.resources.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.utah.nanofab.coralapiserver.core.PasswordResetRequest;


public class PasswordResetOperationPost extends ResourceOperation {
 
	public static final Logger logger = LoggerFactory.getLogger(PasswordResetOperationPost.class);
	
	@Override
	public void performOperationImpl() throws Exception {
		PasswordResetRequest request = (PasswordResetRequest) this.postedObject.get();
		String memberName = request.getName();
		String newPass = request.getPassword();
		
		try {
			logger.debug("Updating password for user '" + memberName + "' with new password '" + newPass + "'");
			this.api.updatePassword(memberName, newPass);
		} catch(Exception e) {
			logger.error("Error: Could not update password for user '" + memberName + "'");
			logger.trace(e.getMessage(), e);
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
