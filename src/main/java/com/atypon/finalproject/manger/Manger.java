package com.atypon.finalproject.manger;

public interface Manger {

     void giveUserWritePrivilege(String user);
     boolean validateUser(String name, String pass);
     void addUser(String username, String password);
     void resetPass(String name, String pass);
}
