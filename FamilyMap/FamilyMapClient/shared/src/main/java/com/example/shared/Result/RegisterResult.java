package com.example.shared.Result;

import com.google.gson.annotations.SerializedName;

/**
 *  Represents the result for the register method
 *  This class extends the Response class
 */
public class RegisterResult extends Result {
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
