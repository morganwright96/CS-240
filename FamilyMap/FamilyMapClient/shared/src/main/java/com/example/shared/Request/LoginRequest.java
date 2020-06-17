package com.example.shared.Request;

/**
 * This is the class for a login request
 */
public class LoginRequest {
    private String userName = "";
    private String password = "";

    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
