package edu.utah.nanofab.coralapiserver.core;

public class GenericRoleRequest {
  private String role;
  private String member;
  private String target;
  
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
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }
}
