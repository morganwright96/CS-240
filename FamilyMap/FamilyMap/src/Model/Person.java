package Model;


/**
 * This represents a basic person class
 * This class allows the creation of a person object
 * MotherID, FatherID, and SpouseID are optional fields from the Database.
 */

public class Person {
    private String PersonId = "";
    private String Username = "";
    private String FirstName = "";
    private String LastName = "";
    private Character Gender = null;
    private String FatherID = "";
    private String MotherID = "";
    private String SpouseID = "";

    public String getPersonId() {
        return PersonId;
    }

    public void setPersonId(String personId) {
        PersonId = personId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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

    public Character getGender() {
        return Gender;
    }

    public void setGender(Character gender) {
        Gender = gender;
    }

    public String getFatherID() {
        return FatherID;
    }

    public void setFatherID(String fatherID) {
        FatherID = fatherID;
    }

    public String getMotherID() {
        return MotherID;
    }

    public void setMotherID(String motherID) {
        MotherID = motherID;
    }

    public String getSpouseID() {
        return SpouseID;
    }

    public void setSpouseID(String spouseID) {
        SpouseID = spouseID;
    }
}
