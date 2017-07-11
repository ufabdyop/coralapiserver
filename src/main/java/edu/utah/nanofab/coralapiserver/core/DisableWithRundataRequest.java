package edu.utah.nanofab.coralapiserver.core;

public class DisableWithRundataRequest {
  public String item;
  public String rundataId;

    public String getRundataId() {
        return rundataId;
    }

    public void setRundataId(String rundataId) {
        this.rundataId = rundataId;
    }
  public String getItem() {
    return item;
  }
  public void setItem(String item) {
    this.item = item;
  }
}
