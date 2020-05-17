package Service.Request;

import Model.Event;
import Model.Person;
import Model.User;

import java.util.ArrayList;

/**
 * This is the request to load information into the database
 * This class is given three list of objects
 */
public class LoadRequest {
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<Person> personList = new ArrayList<>();
    private ArrayList<Event> eventList = new ArrayList<>();

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public ArrayList<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(ArrayList<Person> personList) {
        this.personList = personList;
    }

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }
}
