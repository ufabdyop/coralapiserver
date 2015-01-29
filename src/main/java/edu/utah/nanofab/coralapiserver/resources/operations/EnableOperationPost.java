package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.EnableRequest;

public class EnableOperationPost extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      EnableRequest enableRequest = (EnableRequest)(this.postedObject.get());
      System.out.println("Enabling beginning for " + enableRequest.getItem());
  
      this.api.enable(user.getUsername(), enableRequest.getMember(), 
          enableRequest.getProject(), enableRequest.getItem());
      this.setReturnValue(enableRequest);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to enable" ;
  }

}
