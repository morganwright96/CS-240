package DAO;

import Model.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * This class is used to access the info in the users table
 * This class supports Insert, Delete, and Get methods
 */

public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn){
        this.conn = conn;
    }
    /**
     * This inserts a new person into the person table
     * @param newPerson A new person created by the Service package
     */
    public void insert(Person newPerson) throws DataAccessException {
        String sql = "INSERT INTO Person (PersonID, Username, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, newPerson.getPersonID());
            stmt.setString(2, newPerson.getUserName());
            stmt.setString(3, newPerson.getFirstName());
            stmt.setString(4, newPerson.getLastName());
            stmt.setString(5, newPerson.getGender().toString());
            stmt.setString(6, newPerson.getFatherID());
            stmt.setString(7, newPerson.getMotherID());
            stmt.setString(8, newPerson.getSpouseID());
            // try to insert into the database
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Clears the Persons table in the database
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing Person Table");
        }
    }

    /**
     * Delete any people that are associated with the current username
     * @param username the username for the events people to delete
     */
    public void delete(String username) throws DataAccessException {
        String sql = "Delete FROM Person WHERE Username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("There was a problem deleting the info from the database for the user");
        }
    }

    /**
     * Allow you to get the person object based on the personID
     * @param personID A specific person in the database
     * @return The person object
     */
    public Person getSinglePerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("Username"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender").charAt(0),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
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

    /**
     * This gets all the people that are family members with the current user
     * @param username for the current user
     * @return The list of Person objects
     */
    public ArrayList<Person> getAllPeople(String username) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        ArrayList<Person> peopleList = new ArrayList<>();
        String sql = "SELECT * FROM Person WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()){
                person = new Person(rs.getString("PersonID"), rs.getString("Username"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender").charAt(0),
                        rs.getString("FatherID"), rs.getString("MotherID"), rs.getString("SpouseID"));
                peopleList.add(person);
            }
            if(peopleList.size() > 0){
                return peopleList;
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
