package Model;

import java.util.Objects;

/**
 *  This is a basic AuthToken object
 *  This class represents a users authentication with the server
 *  This class uses a token based system based on the person id and a primary value
 */

public class AuthToken {
    private String PersonID = "";
    private String AuthToken = "";

    /**
     * This creats an auth token for the given user
     * @param personID The id of the user
     * @param authToken The token for the user
     */
    public AuthToken(String personID, String authToken) {
        PersonID = personID;
        AuthToken = authToken;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(PersonID, authToken.PersonID) &&
                Objects.equals(AuthToken, authToken.AuthToken);
    }
}
