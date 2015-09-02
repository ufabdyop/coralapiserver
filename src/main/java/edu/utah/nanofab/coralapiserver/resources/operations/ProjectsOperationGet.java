package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Projects;

public class ProjectsOperationGet extends ResourceOperation {

  @Override
  public void performOperationImpl() throws Exception {
    
    /* If the member name was supplied, then only return the projects that member
     * is currently working on. Otherwise, return all of the active projects.
     */
    if (this.queryParam.isPresent()) {
      this.logger.debug("calling get member projects on " + this.queryParam.get());
      Projects projects = this.api.getMemberProjects(this.queryParam.get());
      System.out.println("Got Projects Collection of Size: " + projects.size());
      this.setReturnValue(projects);
    } else {
      this.logger.debug("calling getProjects" );
      Projects allProj = this.api.getProjects();
      this.logger.debug("result size of getProjects: " + allProj.size() );
      this.setReturnValue(allProj);
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
