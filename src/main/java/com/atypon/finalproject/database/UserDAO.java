package com.atypon.finalproject.database;

import com.atypon.finalproject.models.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class UserDAO implements UserDAOInterface {

  private static final HashMap<String, User> users = new HashMap<>();

  private static final UserDAO userDao = new UserDAO();

  private UserDAO() {
    User admin = new User("admin", "admin");
    admin.setAdmin(true);
    users.put("admin", admin);
  }

  public static UserDAO getInstance() {
    return userDao;
  }

  @Override
  public User getUser(String user) {
    return users.get(user);
  }

  @Override
  public synchronized void addUsers(User user) {
    users.put(user.getUsername(), user);
    for (User u : users.values()) {
      System.out.println(u.getUsername());
    }
  }

  @Override
  public boolean containUser(String username) {
    return users.containsKey(username);
  }
}
