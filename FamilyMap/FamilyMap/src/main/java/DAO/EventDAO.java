package DAO;

import Model.Event;

import java.util.ArrayList;

/**
 * This class allows access to the events table in the database
 * This class supports the Insert, Delete, and Get methods
 */

public class EventDAO {

    /**
     * Create a new event and insert it into the event table
     * @param myEvent The event created by the Service package
     */
    public void insert(Event myEvent){

    }

    /**
     * This clears the Event table
     */
    public void clear(){

    }

    /**
     * This will delete any events that were created by a given user
     * @param username The username for the current user
     */
    public void delete(String username){

    }

    /**
     * This gets a single event based on the eventID
     * @param eventID the id for the desired event
     * @return An event object
     */
    public Event getEvent(String eventID){
        return null;
    }

    /**
     * This gets a list of all the event for all members of the current user
     * @param username the current users username
     * @return A list of Event objects
     */
    public ArrayList<Event> getAllEvents(String username){
        return null;
    }
}
