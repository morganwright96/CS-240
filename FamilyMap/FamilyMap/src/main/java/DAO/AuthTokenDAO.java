package DAO;

import Model.AuthToken;

import java.sql.*;

/**
 * This is the class to access data in the Auth Token table of the database
 * This class supports Insert, Delete, and Get methods
 */

public class AuthTokenDAO {

    private final Connection conn;

    public AuthTokenDAO(Connection conn){
        this.conn = conn;
    }
    /**
     * This inserts a new token into the database for the current user
     * @param newToken The new token created by the Service package
     */
    public void insert(AuthToken newToken) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (PersonID, AuthToken) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, newToken.getPersonID());
            stmt.setString(2, newToken.getAuthToken());
            // try to insert into the database
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * This clears the AuthToken table in the database
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM AuthToken";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing AuthToken Table");
        }
    }

    /**
     * This allows you to return the auth token for the given person
     * @param personID The person needing an AuthToken
     * @return The auth token
     */
    public AuthToken getAuthToken(String personID) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("PersonID"), rs.getString("AuthToken"));
                return authToken;
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
