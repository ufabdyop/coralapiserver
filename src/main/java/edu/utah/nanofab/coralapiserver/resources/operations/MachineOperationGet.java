package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Machines;
import edu.utah.nanofab.coralapi.resource.Machine;

public class MachineOperationGet extends ResourceOperation {

  @Override
  public void performOperationImpl() throws Exception {
    if (this.queryParam.isPresent()) {
      Machines temp = new Machines();
      temp.add(this.api.getMachine(this.queryParam.get()));
      this.setReturnValue(temp);
    } else {
      this.setReturnValue(this.api.getAllMachines());
    }
  }

  @Override
  public String errorMessage() {
    return "Error while trying to get machine ";
  }
  
}