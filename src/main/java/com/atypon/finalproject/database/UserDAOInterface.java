package com.atypon.finalproject.database;

import com.atypon.finalproject.users.User;

public interface UserDAOInterface {

  User getUser(String user);

  void addUsers(User user);

  boolean containUser(String username);
}
