package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.DisableRequest;

public class DisableOperationPost extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      DisableRequest disableRequest = (DisableRequest)(this.postedObject.get());
      System.out.println("Disabling beginning for " + disableRequest.getItem());
  
      this.api.disable(user.getUsername(), disableRequest.getItem());
      this.setReturnValue(disableRequest);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to disable" ;
  }

}
