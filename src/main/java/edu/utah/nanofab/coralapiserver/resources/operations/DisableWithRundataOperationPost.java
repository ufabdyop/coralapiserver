package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapiserver.core.DisableWithRundataRequest;
import java.util.HashMap;

public class DisableWithRundataOperationPost extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
      DisableWithRundataRequest disableRequest = (DisableWithRundataRequest)(this.postedObject.get());
      System.out.println("Disabling beginning for " + disableRequest.getItem());
  
      //String id = this.api.disable(user.getUsername(), disableRequest.getItem());
      String id = this.api.disableWithRundata(user.getUsername(), 
              disableRequest.getItem(), 
              disableRequest.getRundataId());
      
      HashMap<String, String> result = new HashMap<String, String>();
      result.put("id", id);
      result.put("success", "true");
      
      this.setReturnValue(result);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to disable" ;
  }

}
