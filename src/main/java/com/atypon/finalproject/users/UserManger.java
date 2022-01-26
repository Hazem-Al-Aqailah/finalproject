package com.atypon.finalproject.users;

import com.atypon.finalproject.json.JsonDAO;

import java.util.Objects;

public class UserManger {
  static JsonDAO dao = JsonDAO.getInstance();

  private UserManger() {}

  public static void giveUserWritePrivilege(String user) {
    dao.getUser(user).setCanWrite();
  }

  public static boolean validateUser(String name, String pass) {
    return Objects.equals(dao.getUser(name).getUsername(), name)
        && Objects.equals(dao.getUser(name).getPassword(), pass);
  }

  public static void addUser(String username, String password) {
    User user = new User(username, password);
    dao.addUsers(user);
  }

  public static void resetPass(String name, String pass) {
    try {
      User u = dao.getUser(name);
      if (u.isFirstLogin()) u.setPassword(pass);
      u.setFirstLogin(false);
      dao.addUsers(u);
    } catch (Exception e) {
      System.out.println("no such user exists!");
    }
  }

}
