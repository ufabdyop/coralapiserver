package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.DisableRequest;
import java.util.HashMap;

public class DisableOperationPost extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      DisableRequest disableRequest = (DisableRequest)(this.postedObject.get());
      System.out.println("Disabling beginning for " + disableRequest.getItem());
  
      String id = this.api.disable(user.getUsername(), disableRequest.getItem());
      HashMap<String, String> result = new HashMap<String, String>();
      result.put("id", id);
      this.setReturnValue(result);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to disable" ;
  }

}
