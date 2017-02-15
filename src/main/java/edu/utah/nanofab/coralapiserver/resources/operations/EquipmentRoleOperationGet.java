package edu.utah.nanofab.coralapiserver.resources.operations;

public class EquipmentRoleOperationGet extends ResourceOperation {

  @Override
  public void performOperationImpl() throws Exception {
    this.setReturnValue(this.api.getEquipmentRoles(this.queryParam.get()));
  }

  @Override
  public String errorMessage() {
    return "Error while trying to get member with name: " + this.queryParam.get();
  }
  
}