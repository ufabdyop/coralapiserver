package edu.utah.nanofab.coralapiserver.core;

public class ProjectRoleRequest {
  private String role;
  private String member;
  private String project;
  
  public String getRole() {
    return role;
  }
  public void setRole(String role) {
    this.role = role;
  }
  public String getMember() {
    return member;
  }
  public void setMember(String member) {
    this.member = member;
  }
  public String getProject() {
    return project;
  }
  public void setProject(String project) {
    this.project = project;
  }
}
