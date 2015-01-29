package edu.utah.nanofab.coralapiserver.auth;

public class User {
  String username;
  
  public User(String username) {
    this.username = username;
  }

  public User() {
    this.setUsername("");
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
