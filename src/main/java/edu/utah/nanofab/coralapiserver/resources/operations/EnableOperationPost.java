package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.EnableRequest;
import java.util.HashMap;

public class EnableOperationPost extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      EnableRequest enableRequest = (EnableRequest)(this.postedObject.get());
      System.out.println("Enabling beginning for " + enableRequest.getItem());
  
      String id = this.api.enable(user.getUsername(), enableRequest.getMember(), 
          enableRequest.getProject(), enableRequest.getItem());
      HashMap<String, String> response = new HashMap<String, String>();
      response.put("id", id);
      this.setReturnValue(response);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to enable" ;
  }

}