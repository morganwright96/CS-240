package DAO;

import Model.AuthToken;

/**
 * This is the class to access data in the Auth Token table of the database
 * This class supports Insert, Delete, and Get methods
 */

public class AuthTokenDao {

    /**
     * This inserts a new token into the database for the current user
     * @param newToken The new token created by the Service package
     */
    public void insert(AuthToken newToken){

    }

    /**
     * This clears the AuthToken table in the database
     */
    public void clear(){

    }

    /**
     * This allows you to return the auth token for the given person
     * @param personID The person needing an AuthToken
     * @return The auth token
     */
    public String getAuthToken(String personID){
        return null;
    }
}
