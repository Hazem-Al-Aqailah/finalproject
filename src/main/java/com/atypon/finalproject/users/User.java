package com.atypon.finalproject.users;

public class User {
  protected String username ;
  protected String password ;
  protected boolean canWrite = false;
  protected boolean isAdmin = false;
  private boolean firstLogin = true;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String passWord) {
    this.password = passWord;
  }

  public boolean canWrite() {
    return canWrite;
  }

  public void setCanWrite() {
    this.canWrite = true;
  }

  public boolean isAdmin() {
    return isAdmin;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }

  public boolean isFirstLogin(){
    return firstLogin;
  }

  public void setFirstLogin(boolean firstLoginBoolean) {
    firstLogin = firstLoginBoolean;
  }



}
