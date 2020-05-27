package DAOTests;

import DAO.AuthTokenDAO;
import DAO.DataAccessException;
import DAO.Database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import Model.AuthToken;

public class AuthTokenDAOTests {

    private Database db;
    private AuthToken newToken;
    Connection conn;
    AuthTokenDAO atDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        newToken = new AuthToken("a", "a");
        //Here, we'll open the connection in preparation for the test case to use it
        conn = db.openConnection();

        //Then we pass that connection to the personDAO so it can access the database
        atDao = new AuthTokenDAO(conn);
        //Let's clear the users table as well so any lingering data doesn't affect our tests
        atDao.clear();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        atDao.insert(newToken);
        //So lets use a find method to get the Person that we just put in back out
        AuthToken compareTest = atDao.getAuthToken(newToken.getPersonID());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(newToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        atDao.insert(newToken);
        //but our sql table is set up so that "PersonID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> atDao.insert(newToken));
    }

    @Test
    public void clearPass() throws DataAccessException{
        // Try to find a person in the database. If there is a person then the test fails
        assertNull(atDao.getAuthToken(newToken.getPersonID()));
    }

    @Test
    public void getTokenPass() throws DataAccessException{
        // Try to find the person in the database based on the personID
        atDao.insert(newToken);
        assertNotNull(atDao.getAuthToken(newToken.getPersonID()));
    }

    @Test
    public void getTokenFail() throws DataAccessException{
        // Try to find the username in the database for the user that we created in setup
        // This should fail because we dont allow for null PersonID
        assertNull(atDao.getAuthToken(""));
    }
}
