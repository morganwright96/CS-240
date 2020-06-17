package DAOTests;

import DAO.DataAccessException;
import DAO.Database;

import DAO.EventDAO;

import com.example.shared.Model.Event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.util.ArrayList;


public class EventDAOTest {

    private Database db;
    private Event newEvent;
    Connection conn;
    EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        newEvent = new Event("a", "morgan", "morgan",
                1.1, 1.1, "USA", "Provo", "Death", 2020);
        //Here, we'll open the connection in preparation for the test case to use it
        conn = db.openConnection();

        //Then we pass that connection to the personDAO so it can access the database
        eDao = new EventDAO(conn);
        //Let's clear the users table as well so any lingering data doesn't affect our tests
        eDao.clear();
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
        eDao.insert(newEvent);
        Event compareTest = eDao.getEvent(newEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(newEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        //lets do this test again but this time lets try to make it fail
        //if we call the method the first time it will insert it successfully
        eDao.insert(newEvent);
        //but our sql table is set up so that "PersonID" must be unique. So trying to insert it
        //again will cause the method to throw an exception
        //Note: This call uses a lambda function. What a lambda function is is beyond the scope
        //of this class. All you need to know is that this line of code runs the code that
        //comes after the "()->" and expects it to throw an instance of the class in the first parameter.
        assertThrows(DataAccessException.class, ()-> eDao.insert(newEvent));
    }

    @Test
    public void clearPass() throws DataAccessException{
        // Clear the database
        eDao.clear();
        // Try to find a person in the database. If there is a person then the test fails
        assertNull(eDao.getEvent(newEvent.getEventID()));
    }

    @Test
    public void getSingleUserPass() throws DataAccessException{
        // Try to find the person in the database based on the personID
        eDao.insert(newEvent);
        assertNotNull(eDao.getEvent(newEvent.getEventID()));
    }

    @Test
    public void getSingleUserFail() throws DataAccessException{
        // Try to find the username in the database for the user that we created in setup
        // This should fail because we dont allow for null PersonID
        assertNull(eDao.getEvent(""));
    }

    @Test
    public void getAllPeoplePass() throws DataAccessException{
        eDao.insert(newEvent);
        Event tempEvent = new Event("b", "bob", "morgan",
                1.1f, 1.1f, "USA", "Provo", "Death" , 2020);
        eDao.insert(tempEvent);
        ArrayList<Event> tempList = eDao.getAllEvents("morgan");
        assertEquals(1, tempList.size());
        tempList = eDao.getAllEvents("bob");
        assertEquals(1, tempList.size());
    }

    @Test void getAllPeopleFail() throws DataAccessException{
        eDao.insert(newEvent);
        ArrayList<Event> tempList = null;
        assertNull(tempList = eDao.getAllEvents("bob"));

    }

}
