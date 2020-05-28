package Model;


import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * This represents a basic person class
 * This class allows the creation of a person object
 * MotherID, FatherID, and SpouseID are optional fields from the Database.
 */

public class Person {
    private String personID = "";
    @SerializedName("associatedUsername")
    private String userName = "";
    private String firstName = "";
    private String lastName = "";
    private Character gender = null;
    private String fatherID = null;
    private String motherID = null;
    private String spouseID = null;

    /**
     * This constructs a new person with the given params
     * @param personID The id for the new person
     * @param userName The username of the current user
     * @param firstName The first name of the person
     * @param lastName The last name of the person
     * @param gender The gender of the person
     * @param fatherID The person id for the father
     * @param motherID The person id for the Mother
     * @param spouseID The person id for the Spouse
     */
    public Person(String personID, String userName, String firstName, String lastName,
                  Character gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) &&
                Objects.equals(userName, person.userName) &&
                Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) &&
                Objects.equals(gender, person.gender) &&
                Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }

}
