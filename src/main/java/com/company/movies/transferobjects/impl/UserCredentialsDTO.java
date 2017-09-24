package com.company.movies.transferobjects.impl;

import com.company.movies.utils.UserUtil;


public class UserCredentialsDTO {


    private String userName;

    private int userId;

    private String userPassword;

    private UserUtil.UserRoles role;

    public UserCredentialsDTO(String userName, int userId, String userPassword, UserUtil.UserRoles role) {
        this.userName = userName;
        this.userId = userId;
        this.userPassword = userPassword;
        this.role = role;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public UserUtil.UserRoles getRole() {
        return role;
    }

    public void setRole(UserUtil.UserRoles role) {
        this.role = role;
    }

    public String printMe() {
        return "User: " + this.userName + " this.userId: " + this.userId + " this.userPassword:" + this.userPassword + " this.role: " + this.role;
    }


}
