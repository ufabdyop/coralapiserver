package edu.utah.nanofab.coralapiserver.resources.operations;

public class RunDataDefinitionOperationGet extends ResourceOperation {

    @Override
    public void performOperationImpl() throws Exception {
            this.setReturnValue(this.api.getRundataProcessesWithDefinitions(this.queryParam.get()));
    }

    @Override
    public String errorMessage() {
            return "Error while trying to get rundata process definition with name: " + this.queryParam.get();
    }
}