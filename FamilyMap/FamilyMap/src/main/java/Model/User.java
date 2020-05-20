package Model;


/**
 *  This is the basic user object.
 *  This class represents the current logged in user for the application
 * */

public class User {

    private String Username = "";
    private String Password = "";
    private String Email = "";
    private String FirstName = "";
    private String LastName = "";
    private String PersonID = "";

    /**
     * Creates a new user with the provided information
     * @param username The provided username must be a unique username
     * @param password The provided password
     * @param email The Provided email
     * @param firstName The provided first name
     * @param lastName The provided last name
     * @param personID The ID for the user
     */
    public User(String username, String password, String email, String firstName, String lastName, String personID) {
        Username = username;
        Password = password;
        Email = email;
        FirstName = firstName;
        LastName = lastName;
        PersonID = personID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPersonID() {
        return PersonID;
    }

    public void setPersonID(String personID) {
        PersonID = personID;
    }

    /**
     * This overrides the equals method and allows the comparing of two objects
     * This compares an object that is passed in with a similar object
     * @param o This is an object of a given type
     * @return If the object is not equal or the object is not an instance of User than return false
     */
    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User oUser = (User) o;
            return oUser.getUsername().equals(getUsername()) &&
                    oUser.getPassword().equals(getPassword()) &&
                    oUser.getEmail().equals(getEmail()) &&
                    oUser.getFirstName().equals(getFirstName()) &&
                    oUser.getLastName().equals(getLastName()) &&
                    oUser.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
    }
}
