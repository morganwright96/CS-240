package Model;


/**
 *  This is the basic user object.
 *  This class represents the current logged in user for the application
 * */

public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String personID;

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
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personID = personID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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
            return oUser.getUserName().equals(getUserName()) &&
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
