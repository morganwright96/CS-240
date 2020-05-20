package DAO;

import Model.User;

import java.sql.*;

/**
 * This class is used to access, and delete data from the user table in the database
 * The methods available are Insert, Delete, and Get
 */

public class UserDAO {
    private final Connection conn;

    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }
    /**
     * This allows you to insert a new user into the table
     * @param newUser a user object created by the service package
     */
    public void insert(User newUser) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO User (UserName, Password, Email, FirstName , LastName, " +
                "PersonID) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, newUser.getUsername());
            stmt.setString(2, newUser.getPassword());
            stmt.setString(3, newUser.getEmail());
            stmt.setString(4, newUser.getFirstName());
            stmt.setString(5, newUser.getLastName());
            stmt.setString(6, newUser.getPersonID());
            // try to insert into the database
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * This clears the user table
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing User Table");
        }
    }

    /**
     * This allows you to try and login to the system and get information from the database
     * This method compares the provided params with the database entry
     * @param username The username provided by the user
     * @param password The password provided by the user
     * @return The auth token with the associated user if the user exists or param are correct
     */
    public String login(String username, String password){
        return null;
    }

    public User find(String username) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE UserName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("UserName"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("PersonID"));
                return user;
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
