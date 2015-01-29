package edu.utah.nanofab.coralapiserver.core;

import edu.utah.nanofab.coralapi.collections.Members;

public class ProjectMembership {
  
  private String project;
  private String[] members;
  
  /**
   * Constructs a new ProjecctMembership instance. By default, this doesn't initialize any of
   * the private fields.
   */
  public ProjectMembership() {
  }
  
  /**
   * Constructs a new ProjectMembership instance with teh supplied project and members.
   * 
   * @param project The coral project.
   * @param members The coral members.
   */
  public ProjectMembership(String project, Members members) {
    this.project = project;
    this.members = members.getNames();
  }
  
  /**
   * Gets the project field from this ProjectMembership.
   * 
   * @return The project
   */
  public String getProject() {
    return project;
  }
  
  /**
   * Sets the project field of this ProjectMembership.
   * 
   * @param project The project to set
   */
  public void setProject(String project) {
    this.project = project;
  }
  
  /**
   * Gets the members field from this ProjectMembership.
   * 
   * @return The members
   */
  public String[] getMembers() {
    return members;
  }
  
  /**
   * Sets the member field of this ProjectMembership.
   * 
   * @param members The members to set
   */
  public void setMembers(String[] members) {
    this.members = members;
  }
}
