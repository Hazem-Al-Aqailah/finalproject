package com.atypon.finalproject.users;

import com.atypon.finalproject.json.JsonDAO;

import java.util.Objects;

public class Admin {
  private static boolean firstLogin = true;
  static JsonDAO dao = JsonDAO.getInstance();
  static Admin admin = new Admin();
  private static String username = "admin";
  private static String password = "admin";

  public Admin() {
  }

  public static Admin getAdmin() {
    return admin;
  }

  public static void giveUserWritePrivilege(String user) {
    dao.getUser(user).setCanWrite();
  }

  public static boolean validateAdmin(String name, String pass) {
    return Objects.equals(username, name) && Objects.equals(password, pass);
  }

  public static boolean validateUser(String name, String pass) {
   try{
     User user = dao.getUser(name);
     return Objects.equals(user.password, pass) && Objects.equals(user.username, name);
   }catch (Exception e){
      System.out.println(e);
      System.out.println("bad user");
   }
    return false;
  }

  public void addUser(String username, String password) {
    User user = new User(username,password);
    dao.addUsers(user);
  }

  public static void resetPass(String pass){
    if (firstLogin) password = pass;
    firstLogin = false;
  }
  public boolean isFirstLogin(){
    return firstLogin;
  }
}
