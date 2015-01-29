package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Members;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;

public class ProjectMembershipOperationPut extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
    ProjectMembership membership = (ProjectMembership) this.postedObject.get();
    name = membership.getProject();
    this.api.addProjectMembers(membership.getProject(), membership.getMembers());
    Members members = this.api.getProjectMembers(membership.getProject());
    this.setReturnValue(new ProjectMembership(membership.getProject(), members));
  }

  @Override
  public String errorMessage() {
    return "Error while trying to add members to project: " + name;
  }

}
