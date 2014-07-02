package edu.utah.nanofab.coralapiserver.resources.operations;

import java.util.Date;

import edu.utah.nanofab.coralapi.resource.Account;
import edu.utah.nanofab.coralapi.resource.Enable;
import edu.utah.nanofab.coralapi.resource.Project;
import edu.utah.nanofab.coralapiserver.core.EnableRequest;

public class EnableOperationPost extends ResourceOperation  {
	@Override
	public void performOperationImpl() throws Exception {
		EnableRequest enableRequest = (EnableRequest)(this.postedObject.get());
		System.out.println("Enabling beginning for " + enableRequest.getItem());
		Enable enableActivity = new Enable();
		enableActivity.setAgent(this.api.getMember(user.getUsername()));
		enableActivity.setMember(this.api.getMember(enableRequest.getMember()));
		enableActivity.setBdate(new Date());
		enableActivity.setItem(enableRequest.getItem());
		
		Project p = this.api.getProject(enableRequest.getProject());
		enableActivity.setProject(p);
		
		Account a =  this.api.getAccount(p.getAccount());
		enableActivity.setAccount(a);
		
		System.out.println("Enabling with activity " + enableRequest.getItem());

		this.api.enable(enableActivity);
		this.setReturnValue(enableActivity);
	}

	@Override
	public String errorMessage() {
		return "Error while trying to enable" ;
	}

}
