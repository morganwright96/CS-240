package Service;

import DAO.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import Model.User;
import Service.Request.LoadRequest;
import Service.Request.LoginRequest;
import Service.Request.RegisterRequest;
import Service.Result.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import org.decimal4j.util.DoubleRounder;

import java.io.FileReader;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;


/**
 * This class handel the different API calls and creates the correct request and responses
 */
public class Services {
    UUIDGenerator uuidGenerator = new UUIDGenerator();
    NameGenerator nameGenerator = new NameGenerator();
    int numGenGenerated = 0;
    int numPeopleAdded = 0;
    int numEventsAdded = 0;
    Person rootPerson = null;
    PersonDao personDAO = null;
    EventDAO eventDAO = null;
    UserDAO userDAO = null;
    AuthTokenDAO authTokenDAO = null;

    ArrayList<JsonObject> locations = new ArrayList<>();

    public Services(Connection conn) throws DataAccessException {
        try{
            userDAO = new UserDAO(conn);
            personDAO = new PersonDao(conn);
            eventDAO = new EventDAO(conn);
            authTokenDAO = new AuthTokenDAO(conn);

            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("json/fnames.json"));
            JsonObject jsonObject = gson.fromJson( reader, JsonObject.class);
            JsonArray json;

            reader = new JsonReader(new FileReader("json/locations.json"));
            jsonObject = gson.fromJson(reader, JsonObject.class);
            json = jsonObject.get("data").getAsJsonArray();
            for(int i = 0; i < json.size(); i++){
                locations.add(json.get(i).getAsJsonObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * This method does the logic for registering for the application
     * @param r A request object for the new user
     * @return The result of trying to add the user
     */
    public RegisterResult register(RegisterRequest r){
        RegisterResult registerResult = new RegisterResult();
        try {
            User user = userDAO.find(r.getUsername());
            if(user == null){
                user = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(), uuidGenerator.getUuid());
                userDAO.register(user);
                rootPerson = new Person(user.getPersonID(), user.getUserName(), user.getFirstName(), user.getLastName(), r.getGender(), null, null, null);
                personDAO.insert(rootPerson);
                LoginRequest loginRequest = new LoginRequest(user.getUserName(), user.getPassword());
                LoginResult loginResult = login(loginRequest);
                if(loginResult.isSuccess()){
                    FillResult fillResult = fill(user.getUserName(), 4);
                    if(fillResult.isSuccess()){
                        registerResult.setAuthToken(loginResult.getAuthToken());
                        registerResult.setPersonID(rootPerson.getPersonID());
                        registerResult.setUsername(user.getUserName());
                        registerResult.setSuccess(true);
                    }
                    else {
                        registerResult.setMessage("Error: There was a problem trying to populate 4 generations of data");
                    }
                }
                else {
                    registerResult.setMessage("Error: There was a problem login in the user after registering");
                }
            }
            else {
                registerResult.setMessage("Error: There is already a user registered with that username.");
            }

        } catch (DataAccessException e) {
            registerResult.setMessage("Error: There was a problem trying to access the database");
            e.printStackTrace();
        }
        return registerResult;
    }

    /**
     * This method does the logic for logging into the application
     * @param r  The login request
     * @return the result of tyring to login to the application
     */
    public LoginResult login(LoginRequest r) throws DataAccessException {
        LoginResult loginResult = new LoginResult();
        try {
            User loginUser = userDAO.login(r.getUserName(), r.getPassword());
            if(loginUser != null){
                loginResult.setSuccess(true);
                loginResult.setUsername(loginUser.getUserName());
                loginResult.setPersonID(loginUser.getPersonID());

                AuthToken token = authTokenDAO.getAuthToken(loginUser.getPersonID());
                // If the user dose not have an existing token create one and add it to the database
                if(token == null){
                    token = new AuthToken(loginUser.getPersonID(), uuidGenerator.getUuid());
                    authTokenDAO.insert(token);
                }
                loginResult.setAuthToken(token.getAuthToken());
            }
            else {
                loginResult.setMessage("Error: The username or password provided was incorrect");
                loginResult.setSuccess(false);
            }

        } catch (DataAccessException e) {
            loginResult.setMessage("Error: There was an error while trying to log you in");
            loginResult.setSuccess(false);
            e.printStackTrace();
        }
        return loginResult;
    }

    /**
     * This method does the logic for clearing the database of all information
     * @return the result of the clear
     */
    public ClearResult clear() throws DataAccessException{
        try {
            // Clear the Tables
            userDAO.clear();
            personDAO.clear();
            eventDAO.clear();
            authTokenDAO.clear();

            // FIXME need to add logic to check the is success
            ClearResult clearResult = new ClearResult();
            clearResult.setMessage("Clear succeeded");
            clearResult.setSuccess(true);
            return clearResult;
        } catch (DataAccessException e) {
            ClearResult clearResult = new ClearResult();
            clearResult.setMessage("There was a problem trying to clear the Database");
            clearResult.setSuccess(false);
            e.printStackTrace();
            return clearResult;
        }
    }

    /**
     * This method does the logic for filling the database with information based on a user
     * @param username the username for the current user
     * @param generations the number of generations we want to populate
     * @return The result of filling the database with information
     */
    public FillResult fill(String username, int generations) throws DataAccessException {
        FillResult fillResult = new FillResult();
        if(generations < 0){
            fillResult.setSuccess(false);
            fillResult.setMessage("You have entered an incorrect number of generations");
            return fillResult;
        }
        try {
            User user = userDAO.find(username);
            if(user == null){
                fillResult.setSuccess(false);
                fillResult.setMessage("Sorry, but there was no user with that username");
                return fillResult;
            }
            Person tempPerson = personDAO.getSinglePerson(user.getPersonID());

            if(tempPerson != null && rootPerson == null){
                rootPerson = tempPerson;
            }

            // rootPerson = personDAO.getSinglePerson(user.getPersonID());
            // Delete all info for the user that is logged in
            personDAO.delete(username);
            eventDAO.delete(username);

            Random random = new Random();
            int year = Calendar.getInstance().get(Calendar.YEAR);
            JsonObject eventJson = getLocationInfo();

            // The user should be 10 years old to use the application but not over 120
            int age = random.nextInt((120-10) + 1) + 10;
            Event birthEvent = new Event(uuidGenerator.getUuid(), user.getUserName(), user.getPersonID(), eventJson.get("latitude").getAsDouble(),
                    eventJson.get("longitude").getAsDouble(), eventJson.get("country").toString(), eventJson.get("city").toString(),"birth" ,year - age);

            if(generations == 0){
                personDAO.insert(rootPerson);
                eventDAO.insert(birthEvent);
                fillResult.setMessage("Successfully added " + numPeopleAdded + " persons and " + numEventsAdded + " events to the database.");
                fillResult.setSuccess(true);
                return fillResult;
            }
            numGenGenerated = 0;
            generateGenerations(generations, rootPerson, year - age);

            personDAO.insert(rootPerson);
            eventDAO.insert(birthEvent);
            numPeopleAdded++;
            numEventsAdded++;

            fillResult.setMessage("Successfully added " + numPeopleAdded + " persons and " + numEventsAdded + " events to the database.");
            fillResult.setSuccess(true);

        } catch (DataAccessException e) {
            e.printStackTrace();
            fillResult.setSuccess(false);
            fillResult.setMessage("Encountered an error during the fill");
        }
        return fillResult;
    }

    /**
     * This method does the logic for loading the database with information
     * @param r This is the load request with the needed information
     * @return The result of the load
     */
    public LoadResult load(LoadRequest r) throws DataAccessException {
        LoadResult loadResult = new LoadResult();
        try {
            if(r.getUserList().size() == 0 || r.getPersonList().size() == 0 || r.getEventList().size() == 0){
                loadResult.setMessage("Failed to load data. One of the required lists was empty");
                loadResult.setSuccess(false);
                return loadResult;
            }

            for(int i = 0; i < r.getUserList().size(); i++){
                User newUser = r.getUserList().get(i);
                userDAO.register(newUser);
            }

            for(int i = 0; i < r.getPersonList().size(); i++){
                Person newPerson = r.getPersonList().get(i);
                personDAO.insert(newPerson);
            }

            for(int i = 0; i < r.getEventList().size(); i++){
                Event newEvent = r.getEventList().get(i);
                eventDAO.insert(newEvent);
            }

            loadResult.setMessage("Successfully added " + r.getUserList().size() + " users, " + r.getPersonList().size() + " persons, and " +
                            r.getEventList().size() + " events to the database.");
            loadResult.setSuccess(true);
            return loadResult;
        } catch (DataAccessException e) {
            loadResult.setMessage("Sorry we could not load the information in the database");
            loadResult.setSuccess(false);
            e.printStackTrace();
        }
        return loadResult;
    }

    /**
     * This method does the logic for getting a person from the database
     * @param personID The id for the person we want to find
     * @return the Person from the database
     */
    public PersonResult person(String personID, Person currentUser){
        PersonResult personResult = new PersonResult();
        try {
            Person person = personDAO.getSinglePerson(personID);
            if(person != null){
                if(person.getUserName().equals(currentUser.getUserName())){
                    personResult.setSuccess(true);
                    personResult.setUsername(person.getUserName());
                    personResult.setFirstName(person.getFirstName());
                    personResult.setLastName(person.getLastName());
                    personResult.setGender(person.getGender());
                    personResult.setFatherID(person.getFatherID());
                    personResult.setMotherID(person.getMotherID());
                    personResult.setSpouseID(person.getSpouseID());
                    personResult.setPersonID(person.getPersonID());
                }
                else {
                    personResult.setMessage("Error: Sorry, the person requested does not belong to you.");
                }
            }
            else {
                personResult.setMessage("Error: There is no person with the provide ID");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            personResult.setMessage("Error: There was a problem trying to access the database");
        }

        return personResult;
    }

    /**
     * This method does the logic for getting all the people based on the username
     * @return The result of getting all people from the database
     */
    public PeopleResult people(String username){
        PeopleResult peopleResult = new PeopleResult();
        try {
            ArrayList<Person> personArrayList = personDAO.getAllPeople(username);
            if(personArrayList.size() == 0){
                peopleResult.setMessage("Error: There is no people with the associated username");
            }
            else {
                peopleResult.setData(personArrayList);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(peopleResult);
                peopleResult.setSuccess(true);
            }
        } catch (DataAccessException e) {
            peopleResult.setMessage("Error: Problem trying to access the database");
            e.printStackTrace();
        }
        return peopleResult;
    }

    /**
     * This method does the logic for getting an event based on an Event id
     * @param eventID The id for the event we want to get
     * @return The result of getting the event
     */
    public EventResult event(String eventID, Person currentUser){
        EventResult eventResult = new EventResult();
        try {
            Event event = eventDAO.getEvent(eventID);
            if(event != null){
                if(event.getUserName().equals(currentUser.getUserName())){
                    eventResult.setSuccess(true);
                    eventResult.setEventID(event.getEventID());
                    eventResult.setUsername(event.getUserName());
                    eventResult.setPersonID(event.getPersonID());
                    eventResult.setLatitude(event.getLatitude());
                    eventResult.setLongitude(event.getLongitude());
                    eventResult.setCountry(event.getCountry());
                    eventResult.setCity(event.getCity());
                    eventResult.setEventType(event.getEventType());
                    eventResult.setYear(event.getYear());
                }
                else {
                    eventResult.setMessage("Error: Sorry, the person requested does not belong to you.");
                }
            }
            else {
                eventResult.setMessage("Error: There is no person with the provide ID");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            eventResult.setMessage("Error: There was a problem trying to access the database");
        }
        return eventResult;
    }

    /**
     * This method does the logic for getting all the events based on a user
     * @return The result of getting all the events
     */
    public AllEventsResult allEvents(String username){
        AllEventsResult allEventsResult = new AllEventsResult();
        try {
            ArrayList<Event> eventArrayList = eventDAO.getAllEvents(username);
            if(eventArrayList.size() == 0){
                allEventsResult.setMessage("Error: There is no people with the associated username");
            }
            else {
                allEventsResult.setData(eventArrayList);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(allEventsResult);
                allEventsResult.setSuccess(true);
            }
        } catch (DataAccessException e) {
            allEventsResult.setMessage("Error: Problem trying to access the database");
            e.printStackTrace();
        }
        return allEventsResult;
    }
    
    private void generateGenerations(int generations, Person currentPerson, int currentYear) {
        try{
            if(numGenGenerated == generations){
                return;
            }
            // Logic for mother
            Person mom = genMom(currentPerson.getUserName());
            currentPerson.setMotherID(mom.getPersonID());
            numPeopleAdded++;
            Event momBirth = genBirthEvent(mom, currentYear);
            eventDAO.insert(momBirth);
            numEventsAdded++;

            // Logic for father
            Person dad = genDad(currentPerson.getUserName());
            currentPerson.setFatherID(dad.getPersonID());
            numPeopleAdded++;
            Event dadBirth = genBirthEvent(dad, currentYear);
            eventDAO.insert(dadBirth);
            numEventsAdded++;

            // Setting Spouses
            mom.setSpouseID(dad.getPersonID());
            dad.setSpouseID(mom.getPersonID());

            // Generate the marriage events
            int marriageYear = genMarriageYear(momBirth.getYear(), dadBirth.getYear());
            JsonObject marriageLocation = getLocationInfo();
            Event momMarriage = new Event(uuidGenerator.getUuid(), mom.getUserName(), mom.getPersonID(), marriageLocation.get("latitude").getAsDouble(),
                    marriageLocation.get("longitude").getAsDouble(), marriageLocation.get("country").toString(), marriageLocation.get("city").toString(),"marriage" ,marriageYear);
            numEventsAdded++;
            Event dadMarriage = new Event(uuidGenerator.getUuid(), dad.getUserName(), dad.getPersonID(), marriageLocation.get("latitude").getAsDouble(),
                    marriageLocation.get("longitude").getAsDouble(), marriageLocation.get("country").toString(), marriageLocation.get("city").toString(),"marriage" ,marriageYear);
            numEventsAdded++;
            eventDAO.insert(momMarriage);
            eventDAO.insert(dadMarriage);

            // Generate the Death Events
            JsonObject deathJson = getLocationInfo();

            int deathAge = getDeathYear(momMarriage, currentYear, momBirth);
            Event momsDeathEvent = new Event(uuidGenerator.getUuid(), mom.getUserName(), mom.getPersonID(), deathJson.get("latitude").getAsDouble(),
                    deathJson.get("longitude").getAsDouble(), deathJson.get("country").toString(), deathJson.get("city").toString(),"death", momBirth.getYear() + deathAge);
            eventDAO.insert(momsDeathEvent);
            numEventsAdded++;

            deathJson = getLocationInfo();
            deathAge = getDeathYear(dadMarriage, currentYear, dadBirth);
            Event dadsDeathEvent = new Event(uuidGenerator.getUuid(), dad.getUserName(), dad.getPersonID(), deathJson.get("latitude").getAsDouble(),
                    deathJson.get("longitude").getAsDouble(), deathJson.get("country").toString(), deathJson.get("city").toString(),"death", dadBirth.getYear() + deathAge);
            eventDAO.insert(dadsDeathEvent);
            numEventsAdded++;

            // Recursive call for the number of generations
            numGenGenerated++;
            currentYear = momBirth.getYear();
            generateGenerations(generations, mom, momBirth.getYear());
            personDAO.insert(mom);
            generateGenerations(generations, dad, dadBirth.getYear());
            personDAO.insert(dad);
            numGenGenerated--;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }


    public Person genMom(String username){
        Person mom = new Person(uuidGenerator.getUuid(), username, nameGenerator.getfName(), nameGenerator.getMaidenName(), 'f', null, null, null);
        return mom;
    }

    public Person genDad(String username){
        Person dad = new Person(uuidGenerator.getUuid(), username, nameGenerator.getfName(), nameGenerator.getSurname(), 'm', null, null, null);
        return dad;
    }

    public Event genBirthEvent(Person currentPerson, int currentYear){
        int birthAge = getBirthAge();
        System.out.print(birthAge);
        JsonObject eventJson = getLocationInfo();
        return new Event(uuidGenerator.getUuid(), currentPerson.getUserName(), currentPerson.getPersonID(), eventJson.get("latitude").getAsDouble(),
                eventJson.get("longitude").getAsDouble(), eventJson.get("country").toString(), eventJson.get("city").toString(),"birth" ,currentYear - birthAge);
    }

    public int getBirthAge(){
        Random random = new Random();
        return random.nextInt((50-13) + 1) + 13;
    }

    public JsonObject getLocationInfo(){
        Random random = new Random();
        return locations.get(random.nextInt(locations.size()));
    }

    public int genMarriageYear(int momBirthYear, int dadBirthYear){
        int age = getMarriageAge();
        int marriageYear = 0;
        if(momBirthYear > dadBirthYear){
            marriageYear = momBirthYear + age;
        }
        else {
            marriageYear = dadBirthYear + age;
        }
        return marriageYear;
    }

    public int getMarriageAge(){
        Random random = new Random();
        return random.nextInt((45-16) + 1) + 16;
    }

    public int getDeathYear(Event marriage, int currentYear, Event parentBirth){
        int minAge = 0;
        int deathAge = 0;

        if(marriage.getYear() > currentYear){
            minAge = marriage.getYear() - parentBirth.getYear();
        }
        else {
            minAge = currentYear - parentBirth.getYear();
        }
        deathAge = getDeathAge(minAge);
        return deathAge;
    }

    public int getDeathAge(int minAge){
        Random random = new Random();
        return random.nextInt((120-minAge) + 1) + minAge;
    }

    public boolean isAuthorized(String authToken) throws DataAccessException {
        if(authTokenDAO.getAuthToken(authToken) != null){
            return true;
        }
        return false;
    }

    public Person getLoggedInUser(String token) throws DataAccessException {
        AuthToken authToken = authTokenDAO.getAuthToken(token);
        return personDAO.getSinglePerson(authToken.getPersonID());
    }

}