package edu.utah.nanofab.coralapiserver.core;

public class ReservationRequest {
  private String item;
  private String member;
  private String bdate;
  private int lengthInMinutes;
  
  public String getBdate() {
	return bdate;
  }
  public void setBdate(String bdate) {
	this.bdate = bdate;
  }
  public int getLengthInMinutes() {
	return lengthInMinutes;
  }
  public void setLengthInMinutes(int lengthInMinutes) {
	this.lengthInMinutes = lengthInMinutes;
  }
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
