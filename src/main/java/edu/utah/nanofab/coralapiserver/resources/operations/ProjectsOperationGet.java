package edu.utah.nanofab.coralapiserver.resources.operations;

public class ProjectsOperationGet extends ResourceOperation {

	@Override
	public void performOperationImpl() throws Exception {
		
		/* If the member name was supplied, then only return the projects that member
		 * is currently working on. Otherwise, return all of the active projects.
		 */
		if (this.queryParam.isPresent()) {
			this.setReturnValue(this.api.getMemberProjects(this.queryParam.get()));
		} else {
			this.setReturnValue(this.api.getProjects());
		}
		
		
	}

	@Override
	public String errorMessage() {
		if (this.queryParam.isPresent()) {
			String member = this.queryParam.get();
			return "Error getting project list for member: '" + member + "'"; 
		} else {
			return "Error getting all projects";
		}
	}

}
