package DAO;

import Model.Event;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class allows access to the events table in the database
 * This class supports the Insert, Delete, and Get methods
 */

public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }
    /**
     * Create a new event and insert it into the event table
     * @param myEvent The event created by the Service package
     */
    public void insert(Event myEvent) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Event (EventID, Username, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, myEvent.getEventID());
            stmt.setString(2, myEvent.getUserName());
            stmt.setString(3, myEvent.getPersonID());
            stmt.setDouble(4, myEvent.getLatitude());
            stmt.setDouble(5, myEvent.getLongitude());
            stmt.setString(6, myEvent.getCountry());
            stmt.setString(7, myEvent.getCity());
            stmt.setString(8, myEvent.getEventType());
            stmt.setInt(9, myEvent.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the Events Table");
        }
    }

    /**
     * This clears the Event table
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Events Table");
        }
    }

    /**
     * This will delete any events that were created by a given user
     * @param username The username for the current user
     */
    public void delete(String username) throws DataAccessException {
        String sql = "Delete FROM Event WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("There was a problem deleting the info from the database for the user");
        }
    }

    /**
     * This gets a single event based on the eventID
     * @param eventID the id for the desired event
     * @return An event object
     */
    public Event getEvent(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("Username"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * This gets a list of all the event for all members of the current user
     * @param username the current users username
     * @return A list of Event objects
     */
    public ArrayList<Event> getAllEvents(String username) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        ArrayList<Event> eventList = new ArrayList<>();
        String sql = "SELECT * FROM Event WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()){
                event = new Event(rs.getString("EventID"), rs.getString("Username"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                eventList.add(event);
            }
            if(eventList.size() > 0){
                return eventList;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}
