package com.example.familymapclient;

import com.example.shared.Model.AuthToken;
import com.example.shared.Model.Event;
import com.example.shared.Model.Person;
import com.example.shared.Request.LoginRequest;
import com.example.shared.Result.AllEventsResult;
import com.example.shared.Result.LoginResult;
import com.example.shared.Result.PeopleResult;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DataCacheTests {
    DataCache dataCache = DataCache.getInstance();
    ServerProxy proxy = new ServerProxy();
    AuthToken authToken = new AuthToken("Sheila_Parker", "94f8ce7a-424d-488a-a136-b917e035880e");
    AllEventsResult allEventsResult = proxy.getAllEvents(authToken, "http://127.0.0.1:8080/event/");
    PeopleResult peopleResult = proxy.getAllPeople(authToken, "http://127.0.0.1:8080/person/");;

    @BeforeEach
    public void setUp() {
        dataCache.populateData(authToken, peopleResult, allEventsResult);
    }

    @AfterEach
    public void tearDown(){
        dataCache.logout();
    }

    @Test
    public void familyRelations1() {
        Person user = dataCache.getUser();
        assertNotNull(user.getFatherID());
        assertNotNull(user.getMotherID());
        assertNotNull(user.getSpouseID());

        Person spouse = dataCache.getSpouse();
        assertNotNull(spouse);
        assertTrue(dataCache.getPersonLookup().size() > 0);
        assertTrue(dataCache.getEventLookup().size() > 0);
    }

    @Test
    public void familyRelations2() {
        Person usersFather = dataCache.getPersonLookup().get(dataCache.getUser().getFatherID());
        assertNotNull(usersFather.getFatherID());
        assertNotNull(usersFather.getMotherID());
        assertNotNull(usersFather.getSpouseID());

        Person usersMother = dataCache.getPersonLookup().get(dataCache.getUser().getFatherID());
        assertNotNull(usersMother.getFatherID());
        assertNotNull(usersMother.getMotherID());
        assertNotNull(usersMother.getSpouseID());
    }

    @Test
    public void maleFilter1() {
        Event spouseEvent = dataCache.getEarliestEvent(dataCache.getSpouse().getPersonID());
        assertNotNull(spouseEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(spouseEvent.getEventID()));
        dataCache.maleEventFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(spouseEvent.getEventID()));
        dataCache.maleEventFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(spouseEvent.getEventID()));
    }

    @Test
    public void maleFilter2() {
        Event userFatherEvent = dataCache.getEarliestEvent(dataCache.getUser().getFatherID());
        assertNotNull(userFatherEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(userFatherEvent.getEventID()));
        dataCache.maleEventFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(userFatherEvent.getEventID()));
        dataCache.maleEventFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(userFatherEvent.getEventID()));
    }

    @Test
    public void femaleFilter1() {
        Event userEvent = dataCache.getEarliestEvent(dataCache.getUser().getPersonID());
        assertNotNull(userEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(userEvent.getEventID()));
        dataCache.femaleEventFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(userEvent.getEventID()));
        dataCache.femaleEventFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(userEvent.getEventID()));
    }

    @Test
    public void femaleFilter2() {
        Event userMotherEvent = dataCache.getEarliestEvent(dataCache.getUser().getMotherID());
        assertNotNull(userMotherEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(userMotherEvent.getEventID()));
        dataCache.femaleEventFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(userMotherEvent.getEventID()));
        dataCache.femaleEventFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(userMotherEvent.getEventID()));
    }

    @Test
    public void fatherSideFilter1() {
        Event fatherEvent = dataCache.getEarliestEvent(dataCache.getUser().getFatherID());
        assertNotNull(fatherEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(fatherEvent.getEventID()));
        dataCache.fatherSideFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(fatherEvent.getEventID()));
        dataCache.fatherSideFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(fatherEvent.getEventID()));
    }

    @Test
    public void fatherSideFilter2() {
        Event fathersMothersEvent = dataCache.getEarliestEvent(dataCache.getPersonLookup().get(dataCache.getUser().getFatherID()).getMotherID());
        assertNotNull(fathersMothersEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(fathersMothersEvent.getEventID()));
        dataCache.fatherSideFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(fathersMothersEvent.getEventID()));
        dataCache.fatherSideFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(fathersMothersEvent.getEventID()));
    }

    @Test
    public void MotherSideFilter1() {
        Event mothersEvent = dataCache.getEarliestEvent(dataCache.getUser().getMotherID());
        assertNotNull(mothersEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(mothersEvent.getEventID()));
        dataCache.motherSideFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(mothersEvent.getEventID()));
        dataCache.motherSideFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(mothersEvent.getEventID()));
    }

    @Test
    public void MotherSideFilter2() {
        Event mothersFathersEvent = dataCache.getEarliestEvent(dataCache.getPersonLookup().get(dataCache.getUser().getMotherID()).getFatherID());
        assertNotNull(mothersFathersEvent);
        assertTrue(dataCache.getFilteredEvents().containsKey(mothersFathersEvent.getEventID()));
        dataCache.motherSideFilter(false);
        assertFalse(dataCache.getFilteredEvents().containsKey(mothersFathersEvent.getEventID()));
        dataCache.motherSideFilter(true);
        assertTrue(dataCache.getFilteredEvents().containsKey(mothersFathersEvent.getEventID()));
    }

    @Test
    public void chronologicalTest1() {
        ArrayList<Event> events = dataCache.getChronologicalEvents(dataCache.getUser().getFatherID());
        assertNotNull(events);
        assertTrue(events.size() == 1);
        Event birth = events.get(0);
        assertTrue(birth.getEventType().equalsIgnoreCase("birth"));
    }

    @Test
    public void chronologicalTest2() {
        ArrayList<Event> events = dataCache.getChronologicalEvents(dataCache.getUser().getPersonID());
        assertNotNull(events);
        assertTrue(events.size() == 5);
        Event birth = events.get(0);
        Event death = events.get(events.size() - 1);
        assertTrue(birth.getEventType().equalsIgnoreCase("birth"));
        assertTrue(death.getEventType().equalsIgnoreCase("death"));
        for(int i = 0; i < events.size() - 1; i++){
            Event current = events.get(i);
            Event next = events.get(i + 1);
            if(current.getYear() != next.getYear()){
                assertTrue(current.getYear() < next.getYear());
            }
        }
    }

    @Test
    public void searchPeople1() {
        ArrayList<Person> people = dataCache.getSearchPeople("Rodham");
        assertTrue(people.size() == 2);
        people = dataCache.getSearchPeople("Jones");
        assertTrue(people.size() == 2);
    }

    @Test
    public void searchPeople2() {
        ArrayList<Person> people = dataCache.getSearchPeople("P");
        assertTrue(people.size() == 1);
        people = dataCache.getSearchPeople("M");
        assertTrue(people.size() == 4);
    }

    @Test
    public void searchEvent1() {
        ArrayList<Event> events = dataCache.getSearchEvents("19");
        assertTrue(events.size() == 5);
        events = dataCache.getSearchEvents("qaa");
        assertTrue(events.size() == 2);
    }

    @Test
    public void searchEvent2() {
        ArrayList<Event> events = dataCache.getSearchEvents("birth");
        assertTrue(events.size() == 3);
        events = dataCache.getSearchEvents("b");
        assertTrue(events.size() == 8);
    }
}
