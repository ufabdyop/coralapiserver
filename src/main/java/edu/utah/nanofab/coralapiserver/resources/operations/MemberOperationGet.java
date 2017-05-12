package edu.utah.nanofab.coralapiserver.resources.operations;

public class MemberOperationGet extends ResourceOperation {

  @Override
  public void performOperationImpl() throws Exception {
    if (this.queryParam.isPresent()) {
        this.setReturnValue(this.api.getMember(this.queryParam.get()));
    } else {
        this.setReturnValue(this.api.getAllMembers());
    }
  }

  @Override
  public String errorMessage() {
    return "Error while trying to get member with name: " + this.queryParam.get();
  }
  
}