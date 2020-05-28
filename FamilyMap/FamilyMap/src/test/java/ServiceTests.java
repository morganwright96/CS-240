
import DAO.*;

import Handlers.JsonEncoder;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.LoadRequest;
import Service.Request.LoginRequest;
import Service.Result.ClearResult;
import Service.Result.LoadResult;
import Service.Result.LoginResult;
import Service.Services;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTests {
    private Database db;
    private User user;
    private AuthToken authToken;
    private Event event;
    private Person person;
    private Connection conn;
    private UserDAO userDAO;
    private PersonDao personDao;
    private EventDAO eventDAO;
    private AuthTokenDAO authTokenDAO;
    private Services services;
    String compareString;
    String result;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        conn = db.openConnection();
        services = new Services(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
        compareString = "";
        result = "";
    }

    @Test
    public void clearDatabasePass() throws DataAccessException {
        userDAO = new UserDAO(conn);
        ClearResult clearResult = services.clear();
        result = JsonEncoder.serialize(clearResult, ClearResult.class);
        ClearResult compareResult = new ClearResult();
        compareResult.setSuccess(true);
        compareResult.setMessage("Clear succeeded");
        compareString = JsonEncoder.serialize(compareResult, ClearResult.class);
        assertEquals(compareString, result);
        assertNull(userDAO.find("Morgan"));
    }

    @Test
    public void loadPass() throws DataAccessException {
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();

        User user = new User("Morgan", "Wright", "morgan@byu.edu", "Morgan", "Wright", "Morgan_Wright");
        Person person = new Person("Morgan_Wright", "Morgan", "Morgan", "Wright", 'M', null, null, null);
        Event event = new Event("Morgan_Birth", "Morgan", "Morgan_Wright", 1.1f, 1.1f, "USA", "Salt Lake City", "Birth", 1996);

        users.add(user);
        people.add(person);
        events.add(event);
        LoadRequest loadRequest = new LoadRequest(users, people, events);
        LoadResult loadResult = services.load(loadRequest);
        result = JsonEncoder.serialize(loadResult, LoadResult.class);
        LoadResult compareResult = new LoadResult();
        compareResult.setSuccess(true);
        compareResult.setMessage("Successfully added 1 users, 1 persons, and 1 events to the database.");
        compareString = JsonEncoder.serialize(compareResult, LoadResult.class);

        assertEquals(compareString, result);
    }

    @Test
    public void loadFail() throws DataAccessException {
        ArrayList<Person> people = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Event> events = new ArrayList<>();
        // Try to add empty lists
        LoadRequest loadRequest = new LoadRequest(users, people, events);
        LoadResult loadResult = services.load(loadRequest);
        result = JsonEncoder.serialize(loadResult, LoadResult.class);
        LoadResult compareResult = new LoadResult();
        compareResult.setSuccess(false);
        compareResult.setMessage("Failed to load data. One of the required lists was empty");
        compareString = JsonEncoder.serialize(compareResult, LoadResult.class);
        assertEquals(compareString, result);

    }

    @Test
    public void loginPass() throws DataAccessException {
        User user = new User("Morgan", "Wright", "morgan@byu.edu", "Morgan", "Wright", "Morgan_Wright");
        userDAO = new UserDAO(conn);
        userDAO.register(user);
        LoginRequest loginRequest = new LoginRequest("Morgan", "Wright");
        LoginResult loginResult = services.login(loginRequest);
        result = JsonEncoder.serialize(loginResult, LoginResult.class);
        assertTrue(result.contains("Morgan"));
    }

    @Test
    public void loginFail() throws DataAccessException {
        User user = new User("Morgan", "Wright", "morgan@byu.edu", "Morgan", "Wright", "Morgan_Wright");
        userDAO = new UserDAO(conn);
        userDAO.register(user);
        LoginRequest loginRequest = new LoginRequest("morgan", "BADPass");
        LoginResult loginResult = services.login(loginRequest);
        result = JsonEncoder.serialize(loginResult, LoginResult.class);
        LoginResult compareResult = new LoginResult();
        compareResult.setSuccess(false);
        compareResult.setMessage("Error: The username or password provided was incorrect");
        compareString = JsonEncoder.serialize(compareResult, LoginResult.class);
        assertEquals(compareString, result);
    }

    @Test
    public void fillPass() throws DataAccessException {

    }

    @Test
    public void fillFail() throws DataAccessException {

    }
}
