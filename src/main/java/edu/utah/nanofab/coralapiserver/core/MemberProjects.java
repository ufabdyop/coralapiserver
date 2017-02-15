package edu.utah.nanofab.coralapiserver.core;

import edu.utah.nanofab.coralapi.collections.Projects;

public class MemberProjects {
  
  private String member;
  private String[] projects;
  
  /**
   * Constructs a new ProjecctMembership instance. By default, this doesn't initialize any of
   * the private fields.
   */
  public MemberProjects() {
  }
  
  /**
   * Constructs a new ProjectMembership instance with teh supplied project and members.
   * 
   * @param project The coral project.
   * @param members The coral members.
   */
  public MemberProjects(String member, Projects projects) {
    this.member = member;
    this.projects = projects.getNames();
  }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }
}
