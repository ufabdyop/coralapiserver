package edu.utah.nanofab.coralapiserver.core;

public class EnableRequest {
  private String item;
  private String member;
  public String getItem() {
    return item;
  }
  public void setItem(String item) {
    this.item = item;
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
  private String project;
}
