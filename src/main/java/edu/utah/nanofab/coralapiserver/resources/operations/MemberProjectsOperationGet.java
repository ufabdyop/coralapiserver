package edu.utah.nanofab.coralapiserver.resources.operations;

import edu.utah.nanofab.coralapi.collections.Members;
import edu.utah.nanofab.coralapi.collections.Projects;
import edu.utah.nanofab.coralapiserver.core.MemberProjects;
import edu.utah.nanofab.coralapiserver.core.ProjectMembership;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MemberProjectsOperationGet extends ResourceOperation  {
    /**
     * Sets return value as HashMap<String, ArrayList<String>> object from member to projects
     * @throws Exception 
     */
  @Override
  public void performOperationImpl() throws Exception {
    HashMap<String, ArrayList<String>> requestedMemberProjects = new HashMap<String, ArrayList<String>>();
    if (this.queryParam.isPresent()) {
        String memberName = this.queryParam.get();
        Projects projects = this.api.getMemberProjects(memberName);
        MemberProjects mp = new MemberProjects(memberName, projects);
        requestedMemberProjects.put(memberName, 
                new ArrayList<String>(Arrays.asList(mp.getProjects())));
    } else {
        requestedMemberProjects = this.api.getAllMemberProjects();
    }
    this.setReturnValue(requestedMemberProjects);
  }

  @Override
  public String errorMessage() {
    return "Error while trying to add projects to member: " + name;
  }

}
