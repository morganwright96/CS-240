package DAO;

import Model.Person;

import java.util.ArrayList;

/**
 * This class is used to access the info in the users table
 * This class supports Insert, Delete, and Get methods
 */

public class PersonDao {

    /**
     * This inserts a new person into the person table
     * @param newPerson A new person created by the Service package
     */
    public void insert(Person newPerson){

    }

    /**
     * Clears the Persons table in the database
     */
    public void clear(){

    }

    /**
     * Delete any people that are associated with the current username
     * @param username the current users username
     */
    public void delete(String username){

    }

    /**
     * Allow you to get the person object based on the personID
     * @param personID A specific person in the database
     * @return The person object
     */
    public Person getSinglePerson(String personID){
        return null;
    }

    /**
     * This gets all the people that are family members with the current user
     * @param username for the current user
     * @return The list of Person objects
     */
    public ArrayList<Person> getAllPeople(String username){
        return null;
    }
}
