package com.example.shared.Result;

import com.google.gson.annotations.SerializedName;

/**
 * This class is the result of trying to login
 */

public class LoginResult extends Result {
    private String authToken = null;
    @SerializedName("userName")
    private String username = null;
    private String personID = null;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
