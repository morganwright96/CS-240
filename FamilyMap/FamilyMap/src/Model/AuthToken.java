package Model;

/**
 *  This is a basic AuthToken object
 *  This class represents a users authentication with the server
 *  This class uses a token based system based on the person id and a primary value
 */

public class AuthToken {
    private String PersonID = "";
    private String AuthToken = "";

    public String getPersonID() {
        return PersonID;
    }

    public void setPersonID(String personID) {
        PersonID = personID;
    }

    public String  getAuthToken() {
        return AuthToken;
    }

    public void setAuthToken(String authToken) {
        AuthToken = authToken;
    }
}
