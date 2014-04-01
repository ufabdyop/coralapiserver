package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.nanofab.utah.coralapi.resource.Member;

public class MemberOperationPut extends ResourceOperation  {
	@Override
	public void performOperationImpl() throws Exception {
		Member member = (Member)(this.postedObject.get());
		name = member.getName();
		this.api.updateMember(member);
		this.setReturnValue(this.api.getMember(name));
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get member with name: " + name;
	}

}
