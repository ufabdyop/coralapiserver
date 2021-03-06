package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Members;
import edu.utah.nanofab.coralapiserver.core.MemberProjects;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;

public class MemberProjectsOperationPut extends ResourceOperation  {
  @Override
  public void performOperationImpl() throws Exception {
    MemberProjects membership = (MemberProjects) this.postedObject.get();
    name = membership.getMember();
    String[] projects = membership.getProjects();
    this.api.addMemberProjects(name, projects);
    this.setReturnValue(membership);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to add projects to member: " + name;
  }

}
