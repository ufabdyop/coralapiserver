package edu.utah.nanofab.coralapiserver.resources.operations;

public class MachineOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		if (this.queryParam.isPresent()) {
			this.setReturnValue(this.api.getMachine(this.queryParam.get()));
		} else {
			this.setReturnValue(this.api.getAllMachines());
		}
	}

	@Override
	public String errorMessage() {
		return "Error while trying to get machine ";
	}
	
}