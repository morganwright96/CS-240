
import DAO.*;

import Handlers.JsonEncoder;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.LoadRequest;
import Service.Request.LoginRequest;
import Service.Request.RegisterRequest;
import Service.Result.*;
import Service.Services;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;

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
        personDao = new PersonDao(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        userDAO = new UserDAO(conn);
        eventDAO = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
        compareString = "";
        result = "";
    }

    @Test
    public void clearDatabasePass1() throws DataAccessException {
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
    public void clearDatabasePass2() throws DataAccessException {
        ClearResult clearResult = services.clear();
        result = JsonEncoder.serialize(clearResult, ClearResult.class);
        assertTrue(clearResult.isSuccess());


        ClearResult compareResult = new ClearResult();
        compareResult.setSuccess(true);
        compareResult.setMessage("Clear succeeded");
        compareString = JsonEncoder.serialize(compareResult, ClearResult.class);

        assertEquals(compareString, result);
        assertNull(userDAO.find("Morgan"));
        personDao = new PersonDao(conn);
        eventDAO = new EventDAO(conn);


        assertNull(personDao.getAllPeople("Morgan"));
        assertNull(eventDAO.getAllEvents("Morgan"));
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
        userDAO.register(user);
        LoginRequest loginRequest = new LoginRequest("Morgan", "Wright");
        LoginResult loginResult = services.login(loginRequest);
        result = JsonEncoder.serialize(loginResult, LoginResult.class);
        assertTrue(result.contains("Morgan"));
    }

    @Test
    public void loginFail() throws DataAccessException {
        User user = new User("Morgan", "Wright", "morgan@byu.edu", "Morgan", "Wright", "Morgan_Wright");
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
        RegisterResult registerResult = services.register(createRegisterRequest());
        assertTrue(registerResult.isSuccess());
        FillResult fillResult = services.fill(registerResult.getUsername(), 5);
        assertTrue(fillResult.isSuccess());
        Person person = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(person);
        AuthToken authToken = authTokenDAO.getAuthToken(registerResult.getAuthToken());
        assertNotNull(authToken);
        ArrayList<Event> events = eventDAO.getAllEvents(registerResult.getUsername());
        assertTrue(events.size() > 0);
    }

    @Test
    public void fillFail() throws DataAccessException {
        RegisterResult registerResult = services.register(createRegisterRequest());
        assertTrue(registerResult.isSuccess());

        FillResult fillResult = services.fill(registerResult.getUsername(), -1);
        assertFalse(fillResult.isSuccess());

        fillResult = services.fill("badUserName", 4);
        assertFalse(fillResult.isSuccess());

        fillResult = services.fill(registerResult.getUsername(), 2);
        assertTrue(fillResult.isSuccess());

        Person person = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(person);

        AuthToken authToken = authTokenDAO.getAuthToken(registerResult.getAuthToken());
        assertNotNull(authToken);

        ArrayList<Event> events = eventDAO.getAllEvents(registerResult.getUsername());
        assertTrue(events.size() > 0);
    }

    @Test
    public void registerPass() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());

        User user = new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(), registerRequest.getFirstName(),
                registerRequest.getLastName(), registerResult.getPersonID());
        assertNotNull(user);
        String result = JsonEncoder.serialize(user, User.class);

        User compareUser = userDAO.find(registerResult.getUsername());
        assertNotNull(compareUser);
        compareString = JsonEncoder.serialize(compareUser, User.class);

        assertEquals(result, compareString);

        Person person = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(person);
        AuthToken authToken = authTokenDAO.getAuthToken(registerResult.getAuthToken());
        assertNotNull(authToken);
    }

    @Test
    public void registerFail() throws DataAccessException {
        personDao = new PersonDao(conn);
        authTokenDAO = new AuthTokenDAO(conn);
        userDAO = new UserDAO(conn);
        // register a new user
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());

        // store the user info in a user
        User user = new User(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail(), registerRequest.getFirstName(),
                registerRequest.getLastName(), registerResult.getPersonID());
        assertNotNull(user);
        String result = JsonEncoder.serialize(user, User.class);

        String username = registerResult.getUsername();
        // Check to make sure there is a person and an authtoken
        Person person = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(person);
        AuthToken authToken = authTokenDAO.getAuthToken(registerResult.getAuthToken());
        assertNotNull(authToken);

        // Try to register the user again
        registerResult = services.register(registerRequest);
        assertFalse(registerResult.isSuccess());

        // make sure there is still a user in the database for the firs register
        User compareUser = userDAO.find(username);
        assertNotNull(compareUser);
        compareString = JsonEncoder.serialize(compareUser, User.class);
        assertEquals(result, compareString);
    }

    @Test
    public void getPersonPass() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        Person currentUser = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(currentUser);
        PersonResult fatherResult = services.person(currentUser.getFatherID(), currentUser);
        assertTrue(fatherResult.isSuccess());
        PersonResult motherResult = services.person(currentUser.getMotherID(), currentUser);
        assertTrue(motherResult.isSuccess());
    }

    @Test
    public void getPersonFail() throws DataAccessException {
        Person person = new Person("Morgan_Wright", "Morgan", "Morgan", "Wright", 'm', null, null, null);

        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        Person currentUser = personDao.getSinglePerson(registerResult.getPersonID());

        PersonResult personResult = services.person(person.getPersonID(), person);
        assertFalse(personResult.isSuccess());

        personDao.insert(person);

        personResult = services.person(registerResult.getPersonID(), person);
        assertFalse(personResult.isSuccess());
    }

    @Test
    public void allPeoplePass() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        Person currentUser = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(currentUser);
        PersonResult fatherResult = services.person(currentUser.getFatherID(), currentUser);
        assertTrue(fatherResult.isSuccess());
        PersonResult motherResult = services.person(currentUser.getMotherID(), currentUser);
        assertTrue(motherResult.isSuccess());

        PeopleResult peopleResult = services.people(currentUser.getUserName());
        assertTrue(peopleResult.isSuccess());
        assertTrue(peopleResult.getData().size() > 1);
    }

    @Test
    public void allPeopleFail() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        Person currentUser = personDao.getSinglePerson(registerResult.getPersonID());
        assertNotNull(currentUser);
        PersonResult fatherResult = services.person(currentUser.getFatherID(), currentUser);
        assertTrue(fatherResult.isSuccess());
        PersonResult motherResult = services.person(currentUser.getMotherID(), currentUser);
        assertTrue(motherResult.isSuccess());
        Person person = new Person("Morgan_Wright", "Morgan", "Morgan", "Wright", 'm', null, null, null);
        personDao.insert(person);
        PeopleResult peopleResult = services.people(person.getUserName());
        assertTrue(peopleResult.isSuccess());
        assertTrue(peopleResult.getData().size() == 1);
    }

    @Test
    public void eventPass() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        Event newEvent = new Event("morgans_death", registerResult.getUsername(), registerResult.getPersonID(),40.2444 , 111.6608,
                "United States", "Provo", "death", 2020);
        eventDAO.insert(newEvent);
        EventResult eventResult = services.event(newEvent.getEventID(), personDao.getSinglePerson(registerResult.getPersonID()));
        assertNotNull(eventResult);
        assertTrue(eventResult.isSuccess());

        Event compareEvent = eventDAO.getEvent(eventResult.getEventID());

        String resultString = JsonEncoder.serialize(newEvent, Event.class);
        String compareString = JsonEncoder.serialize(compareEvent, Event.class);
        assertEquals(resultString, compareString);
    }

    @Test
    public void eventFail() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());

        Event newEvent = new Event("morgans_death", registerResult.getUsername(), registerResult.getPersonID(),40.2444 , 111.6608,
                "United States", "Provo", "death", 2020);
        eventDAO.insert(newEvent);
        EventResult eventResult = services.event(newEvent.getEventID(), personDao.getSinglePerson(registerResult.getPersonID()));
        assertNotNull(eventResult);
        assertTrue(eventResult.isSuccess());

        eventResult = services.event("badEventID", personDao.getSinglePerson(registerResult.getPersonID()));
        assertFalse(eventResult.isSuccess());

        Person person = new Person("Morgan_Wright", "Morgan", "Morgan", "Wright", 'm', null, null, null);
        personDao.insert(person);
        eventResult = services.event(newEvent.getEventID(), person);
        assertFalse(eventResult.isSuccess());
    }

    @Test
    public void allEventPass() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        AllEventsResult allEventsResult = services.allEvents(registerResult.getUsername());
        assertTrue(allEventsResult.isSuccess());
        assertTrue(allEventsResult.getData().size() > 1);
        Random random = new Random();
        int getEventObject = random.nextInt(allEventsResult.getData().size());
        Event event = allEventsResult.getData().get(getEventObject);
        assertNotNull(event);

    }

    @Test
    public void allEventFail() throws DataAccessException {
        RegisterRequest registerRequest = createRegisterRequest();
        RegisterResult registerResult = services.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        AllEventsResult allEventsResult = services.allEvents(registerResult.getUsername());
        assertTrue(allEventsResult.isSuccess());
        assertTrue(allEventsResult.getData().size() > 1);

        allEventsResult = services.allEvents("badUserName");
        assertNotNull(allEventsResult);
        assertFalse(allEventsResult.isSuccess());
        assertTrue(allEventsResult.getData().size() == 0);
    }

    public RegisterRequest createRegisterRequest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("bob");
        registerRequest.setFirstName("Morgan");
        registerRequest.setLastName("Morgan");
        registerRequest.setEmail("Morgan@test.com");
        registerRequest.setGender('m');
        registerRequest.setPassword("mysecret");
        return registerRequest;
    }
}
