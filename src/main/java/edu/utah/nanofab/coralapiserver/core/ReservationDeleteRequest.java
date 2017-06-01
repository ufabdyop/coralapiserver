package edu.utah.nanofab.coralapiserver.core;

public class ReservationDeleteRequest {
  private String item;
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
}
