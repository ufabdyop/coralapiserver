package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Members;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;

public class ProjectMembershipOperationDelete extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
    ProjectMembership membership = (ProjectMembership) this.postedObject.get();
    name = membership.getProject();
    logSettings(name, membership);
    this.api.removeProjectMembers(name, membership.getMembers());
    logger.debug("remove Project Members finished. setting return value");
    Members members = this.api.getProjectMembers(name);
    this.setReturnValue(new ProjectMembership(name, members));
  }

  @Override
  public String errorMessage() {
    return "Error while trying to remove members from project: " + name + "\n";
  }
  
  public void logSettings(String project, ProjectMembership membership) {
    logger.debug("ProjectMembershipOperationDelete: project is " + project);
    logger.debug("ProjectMembershipOperationDelete: auth user is " + this.user.getUsername());
      String[] members = membership.getMembers();
    String buffer = "";
      for (String m : members) {
          buffer += m + ", ";
      }
    logger.debug("ProjectMembershipOperationDelete: members are " + buffer);
    
  }

}
