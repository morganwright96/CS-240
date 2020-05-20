package Model;


import java.util.Objects;

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

    /**
     * This constructs a new person with the given params
     * @param personId The id for the new person
     * @param username The username of the current user
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param gender The gender of the person
     * @param fatherID The person id for the father
     * @param motherID The person id for the Mother
     * @param spouseID The person id for the Spouse
     */
    public Person(String personId, String username, String firstName, String lastName,
                  Character gender, String fatherID, String motherID, String spouseID) {
        PersonId = personId;
        Username = username;
        FirstName = firstName;
        LastName = lastName;
        Gender = gender;
        FatherID = fatherID;
        MotherID = motherID;
        SpouseID = spouseID;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(PersonId, person.PersonId) &&
                Objects.equals(Username, person.Username) &&
                Objects.equals(FirstName, person.FirstName) &&
                Objects.equals(LastName, person.LastName) &&
                Objects.equals(Gender, person.Gender) &&
                Objects.equals(FatherID, person.FatherID) &&
                Objects.equals(MotherID, person.MotherID) &&
                Objects.equals(SpouseID, person.SpouseID);
    }

}
