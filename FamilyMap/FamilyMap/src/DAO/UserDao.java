package DAO;

import Model.User;

/**
 * This class is used to access, and delete data from the user table in the database
 * The methods available are Insert, Delete, and Get
 */

public class UserDao {

    /**
     * This allows you to insert a new user into the table
     * @param newUser a user object created by the service package
     */
    public void insert(User newUser){

    }

    /**
     * This clears the user table
     */
    public void clear(){

    }

    /**
     * This allows you to try and login to the system
     * This method compares the provided params with the database entry
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return The auth token with the associated user if the user exists or param are correct
     */
    public String login(String username, String password){
        return null;
    }
}
