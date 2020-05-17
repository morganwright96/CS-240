package Service;

import Model.Person;
import Service.Request.LoadRequest;
import Service.Request.LoginRequest;
import Service.Request.RegisterRequest;
import Service.Result.*;

/**
 * This class handel the different API calls and creates the correct request and responses
 */
public class Services {

    /**
     * This method does the logic for registering for the application
     * @param r A request object for the new user
     * @return The result of trying to add the user
     */
    public RegisterResult register(RegisterRequest r){
        return null;
    }

    /**
     * This method does the logic for logging into the application
     * @param r  The login request
     * @return the result of tyring to login to the application
     */
    public LoginResult login(LoginRequest r){
        return null;
    }

    /**
     * This method does the logic for clearing the database of all information
     * @return the result of the clear
     */
    public ClearResult clear(){
        return null;
    }

    /**
     * This method does the logic for filling the database with information based on a user
     * @param username the username for the current user
     * @param generations the number of generations we want to populate
     * @return The result of filling the database with information
     */
    public FillResult fill(String username, int generations){
        return null;
    }

    /**
     * This method does the logic for loading the database with information
     * @param r This is the load request with the needed information
     * @return The result of the load
     */
    public LoadResult load(LoadRequest r){
        return null;
    }

    /**
     * This method does the logic for getting a person from the database
     * @param personID The id for the person we want to find
     * @return the Person from the database
     */
    public PersonResult person(String personID){
        return null;
    }

    /**
     * This method does the logic for getting all the people base on the username
     * @return The result of getting all people from the database
     */
    public PeopleResult people(){
        return null;
    }

    /**
     * This method does the logic for getting an event based on an Event id
     * @param eventID The id for the event we want to get
     * @return The result of getting the event
     */
    public EventResult event(String eventID){
        return null;
    }

    /**
     * This method does the logic for getting all the events based on a user
     * @return The result of getting all the events
     */
    public AllEventsResult allEvents(){
        return null;
    }
}
