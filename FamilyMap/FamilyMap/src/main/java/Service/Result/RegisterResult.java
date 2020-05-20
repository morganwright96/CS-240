package Service.Result;

/**
 *  Represents the result for the register method
 *  This class extends the Response class
 */
public class RegisterResult extends Result {
    private String authToken = "";
    private String username = "";
    private String personID = "";

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
