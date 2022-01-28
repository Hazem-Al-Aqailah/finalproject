package com.atypon.finalproject.manger;

import com.atypon.finalproject.database.UserDAO;
import com.atypon.finalproject.users.User;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserManger implements Manger {
  static UserDAO userDao = UserDAO.getInstance();
  static UserManger userManger = new UserManger();

  private UserManger() {}

  public static UserManger getManger(){
    return userManger;
  }

  public static boolean containUser(String username) {
    return userDao.containUser(username);
  }

  @Override
  public void giveUserWritePrivilege(String user) {
    userDao.getUser(user).setCanWrite();
  }
  @Override
  public boolean validateUser(String name, String pass) {
    return Objects.equals(userDao.getUser(name).getUsername(), name)
        && Objects.equals(userDao.getUser(name).getPassword(), pass);
  }
  @Override
  public void addUser(String username, String password) {
    User user = new User(username, password);
    userDao.addUsers(user);
  }
  @Override
  public void resetPass(String name, String pass) {
    try {
      User u = userDao.getUser(name);
      if (u.isFirstLogin()) u.setPassword(pass);
      u.setFirstLogin(false);
      userDao.addUsers(u);
    } catch (Exception e) {
      System.out.println("no such user exists!");
    }
  }

}
