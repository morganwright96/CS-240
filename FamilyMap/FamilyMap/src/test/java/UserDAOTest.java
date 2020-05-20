import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;

import Model.User;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;

public class UserDAOTest {
    private Database db;
    private User newUser;
    Connection conn;
    UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        newUser = new User("Morgan", "Testp@ass!", "morgan@test.com",
                "morgan", "wright", "1");
        //Here, we'll open the connection in preparation for the test case to use it
        conn = db.openConnection();

        //Then we pass that connection to the userDAO so it can access the database
        uDao = new UserDAO(conn);
        //Let's clear the users table as well so any lingering data doesn't affect our tests
        uDao.clear();
        uDao.insert(newUser);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //Here we close the connection to the database file so it can be opened elsewhere.
        //We will leave commit to false because we have no need to save the changes to the database
        //between test cases
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        //So lets use a find method to get the event that we just put in back out
        User compareTest = uDao.find(newUser.getUsername());
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(newUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        //but our sql table is set up so that "Username" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> uDao.insert(newUser));
    }

    @Test
    public void clearPass() throws DataAccessException{
        // Try to find a user in the database if there is one then it fail
        uDao.clear();
        // Try to find a user in the database if there is one then it fail
        assertNull(uDao.find(newUser.getUsername()));
    }

    @Test
    public void findPass() throws DataAccessException{
        // Try to find the user in the database
        assertNotNull(uDao.find(newUser.getUsername()));
    }

    @Test
    public void findFail() throws DataAccessException{
        // Try to find the username in the database for the user that we created in setup
        // This should fail because we dont allow for null usernames
        assertNull(uDao.find(null));
    }
}
