package com.example.familymapclient;


import com.example.shared.Model.AuthToken;
import com.example.shared.Model.Event;
import com.example.shared.Model.Person;
import com.example.shared.Result.AllEventsResult;
import com.example.shared.Result.PeopleResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataCache {

    private static DataCache instance;

    private AuthToken authToken;

    private Person user;
    private Person spouse = null;

    private final Set<Person> immediateFamilyMales = new HashSet<>();
    private final Set<Person> immediateFamilyFemales = new HashSet<>();

    private final Set<Person> fatherSideMales = new HashSet<>();
    private final Set<Person> fatherSideFemales = new HashSet<>();
    private final Set<Person> motherSideMales = new HashSet<>();
    private final Set<Person> motherSideFemales = new HashSet<>();

    private final Set<String> eventTypes = new HashSet<>();

    private final Map<String, Person> personLookup = new HashMap<>();
    private final Map<String, Event> eventLookup = new HashMap<>();

    private final Map<String, Event> filteredEvents = new HashMap<>();

    private ArrayList<Person> family = new ArrayList<>();

    private boolean isLifeStoryLines = true;
    private boolean isSpouseLines = true;
    private boolean isFamilyTreeLines = true;
    private boolean isFatherSide = true;
    private boolean isMotherSide = true;
    private boolean isMaleEvents = true;
    private boolean isFemaleEvents = true;

    private String clickedEventID = null;

    private DataCache(){}

    public static DataCache getInstance(){
        if(instance == null){
            instance = new DataCache();
        }
        return instance;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public Person getUser() {
        return user;
    }

    public void populateData(AuthToken token, PeopleResult peopleResult, AllEventsResult allEventsResult){
        authToken = token;
        genPeopleMap(peopleResult);
        genEventMap(allEventsResult);
        user = personLookup.get(authToken.getPersonID());
        genImmediateFamily(user);
        // Start with the fathers side on the gen
        genFatherSide(personLookup.get(user.getFatherID()));
        // Get the mother and gen the family
        genMotherSide(personLookup.get(user.getMotherID()));
        defaultFilteredEvents();
        if(user.getSpouseID() != null){
            spouse = personLookup.get(user.getSpouseID());
        }
    }

    public void genPeopleMap(PeopleResult peopleResult){
        ArrayList<Person> listOfPeople = peopleResult.getData();
        Person currentPerson;
        for(int i = 0; i < listOfPeople.size(); i++){
            currentPerson = listOfPeople.get(i);
            personLookup.put(currentPerson.getPersonID(), currentPerson);
        }
    }

    public void genEventMap(AllEventsResult allEventsResult){
        ArrayList<Event> listOfEvents = allEventsResult.getData();
        Event currentEvent;
        for(int i = 0; i < listOfEvents.size(); i++){
            currentEvent = listOfEvents.get(i);
            eventLookup.put(currentEvent.getEventID(), currentEvent);
            eventTypes.add(currentEvent.getEventType().toLowerCase());
        }
    }

    public void genImmediateFamily(Person currentPerson){
        // If the current user is a male and has a spouse add the spouse to the female immediate list
        if(currentPerson.getGender().equals('m')){
            if(currentPerson.getSpouseID() != null){
                immediateFamilyFemales.add(personLookup.get(currentPerson.getSpouseID()));
            }
        }
        else {
            if(currentPerson.getSpouseID() != null){
                immediateFamilyMales.add(personLookup.get(currentPerson.getSpouseID()));
            }
        }
    }

    public void genFatherSide(Person currentPerson){
        // If the father or mother does not exist
        if(currentPerson == null){
            return;
        }
        // Check the gender to add to correct list
        if(currentPerson.getGender().equals('m')){
            fatherSideMales.add(currentPerson);
        }
        else {
            fatherSideFemales.add(currentPerson);
        }
        genFatherSide(personLookup.get(currentPerson.getFatherID()));
        genFatherSide(personLookup.get(currentPerson.getMotherID()));
    }

    public void genMotherSide(Person currentPerson){
        // If the father or mother does not exist
        if(currentPerson == null){
            return;
        }
        // Check the gender to add to correct list
        if(currentPerson.getGender().equals('m')){
            motherSideMales.add(currentPerson);
        }
        else {
            motherSideFemales.add(currentPerson);
        }
        genMotherSide(personLookup.get(currentPerson.getFatherID()));
        genMotherSide(personLookup.get(currentPerson.getMotherID()));
    }

    public Map<String, Event> getEventLookup() {
        return eventLookup;
    }

    public Set<String> getEventTypes() {
        return eventTypes;
    }

    public Map<String, Person> getPersonLookup() {
        return personLookup;
    }

    public void defaultFilteredEvents(){
        for(Map.Entry<String, Event> entry : eventLookup.entrySet()){
            filteredEvents.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<String, Event> getFilteredEvents(){
        return filteredEvents;
    }

    public boolean isLifeStoryLines() {
        return isLifeStoryLines;
    }

    public void setLifeStoryLines(boolean lifeStoryLines) {
        isLifeStoryLines = lifeStoryLines;
    }

    public boolean isSpouseLines() {
        return isSpouseLines;
    }

    public void setSpouseLines(boolean spouseLines) {
        isSpouseLines = spouseLines;
    }

    public boolean isFamilyTreeLines() {
        return isFamilyTreeLines;
    }

    public void setFamilyTreeLines(boolean familyTreeLines) {
        isFamilyTreeLines = familyTreeLines;
    }

    public boolean isFatherSide() {
        return isFatherSide;
    }

    public void setFatherSide(boolean fatherSide) {
        isFatherSide = fatherSide;
    }

    public boolean isMotherSide() {
        return isMotherSide;
    }

    public void setMotherSide(boolean motherSide) {
        isMotherSide = motherSide;
    }

    public boolean isMaleEvents() {
        return isMaleEvents;
    }

    public void setMaleEvents(boolean maleEvents) {
        isMaleEvents = maleEvents;
    }

    public boolean isFemaleEvents() {
        return isFemaleEvents;
    }

    public void setFemaleEvents(boolean femaleEvents) {
        isFemaleEvents = femaleEvents;
    }

    public void logout(){
        authToken = null;
        isLifeStoryLines = true;
        isFamilyTreeLines = true;
        isSpouseLines = true;
        isFatherSide = true;
        isMotherSide = true;
        isMaleEvents = true;
        isFemaleEvents = true;
        fatherSideMales.clear();
        fatherSideFemales.clear();
        motherSideFemales.clear();
        motherSideMales.clear();
        immediateFamilyMales.clear();
        immediateFamilyFemales.clear();
        eventTypes.clear();
        personLookup.clear();
        eventLookup.clear();
        family.clear();
        filteredEvents.clear();
        spouse = null;
    }

    public Event getEarliestEvent(String personID){
        if(personID == null){
            return null;
        }
        Event spouseEvent = null;
        for(Map.Entry<String,Event> entry: filteredEvents.entrySet()){
            Event currentEvent = entry.getValue();
            if(currentEvent.getPersonID().equals(personID)){
                if(spouseEvent == null){
                    spouseEvent = currentEvent;
                }
                if(currentEvent.getEventType().equals("birth")){
                    return currentEvent;
                }
                if(currentEvent.getYear() < spouseEvent.getYear()){
                    spouseEvent = currentEvent;
                }
            }
        }
        return spouseEvent;
    }

    public String getClickedEventID() {
        return clickedEventID;
    }

    public void setClickedEventID(String clickedEventID) {
        this.clickedEventID = clickedEventID;
    }

    public ArrayList<Event> getChronologicalEvents(String personID){
        ArrayList<Event> listOfEvents = new ArrayList<>();
        Event birth = null;
        Event death = null;
        for(Map.Entry<String, Event> entry : filteredEvents.entrySet()){
            Event currentEvent = entry.getValue();
            if(currentEvent.getPersonID().equals(personID)){
                if(currentEvent.getEventType().equals("birth") || currentEvent.getEventType().equals("death")){
                    if(currentEvent.getEventType().equals("birth")){
                        birth = currentEvent;
                    }
                    if(currentEvent.getEventType().equals("death")){
                        death = currentEvent;
                    }
                }
                else {
                    if(listOfEvents.size() == 0){
                        listOfEvents.add(currentEvent);
                    }
                    else {
                        for(int i = 0; i < listOfEvents.size(); i++){
                            // The year from the map
                            int currentYear = currentEvent.getYear();
                            // The year from the list of events index
                            int tempYear = listOfEvents.get(i).getYear();
                            if(currentYear == tempYear){
                                // do the compare event types
                                int val = listOfEvents.get(i).getEventType().compareToIgnoreCase(currentEvent.getEventType());
                                if(val < 0){
                                    listOfEvents.add(i, currentEvent);
                                }
                                else {
                                    listOfEvents.add(i + 1, currentEvent);
                                }
                                break;
                            }
                            else if(currentYear < tempYear){
                                listOfEvents.add(i, currentEvent);
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(birth != null){
            listOfEvents.add(0, birth);
        }
        if(death != null){
            listOfEvents.add(death);
        }

        return listOfEvents;
    }

    public void removeEvents(Person person){
        ArrayList<String> removeList = new ArrayList<>();
        for(Map.Entry<String, Event> entry : filteredEvents.entrySet()){
            Event currentEvent = entry.getValue();
            if(currentEvent.getPersonID().equals(person.getPersonID())){
                removeList.add(entry.getKey());
            }
        }
        for(int i = 0; i < removeList.size(); i++){
            filteredEvents.remove(removeList.get(i));
        }
    }

    public void addEvents(Person person){
        for (Map.Entry<String, Event> entry : eventLookup.entrySet()) {
            Event currentEvent = entry.getValue();
            if (currentEvent.getPersonID().equals(person.getPersonID())) {
                filteredEvents.put(currentEvent.getEventID(), currentEvent);
            }
        }
    }

    public void fatherSideFilter(boolean isChecked){
        if(isChecked){
            // add conditions to the adds
            if(isMaleEvents){
                for (Person person : fatherSideMales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : fatherSideMales) {
                    removeEvents(person);
                }
            }
            if(isFemaleEvents){
                for (Person person : fatherSideFemales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : fatherSideFemales) {
                    removeEvents(person);
                }
            }
        }
        else {
            for(Person person : fatherSideMales){
                removeEvents(person);
            }
            for(Person person : fatherSideFemales){
                removeEvents(person);
            }
        }
    }

    public void motherSideFilter(boolean isChecked){
        // If filter is true we need to apply that filter
        if(isChecked){
            if(isMaleEvents){
                for (Person person : motherSideMales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : motherSideMales) {
                    removeEvents(person);
                }
            }
            if(isFemaleEvents){
                for (Person person : motherSideFemales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : motherSideFemales) {
                    removeEvents(person);
                }
            }
        }
        else {
            for(Person person : motherSideMales){
                removeEvents(person);
            }
            for(Person person : motherSideFemales){
                removeEvents(person);
            }
        }
    }

    public void maleEventFilter(boolean isChecked){
        if(isChecked){
            if(isFatherSide){
                for (Person person : fatherSideMales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : fatherSideMales) {
                    removeEvents(person);
                }
            }
            if(isMotherSide){
                for (Person person : motherSideMales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : motherSideMales) {
                    removeEvents(person);
                }
            }
            if(user.getGender().equals('m')){
                addEvents(user);
            }
            if(spouse.getGender().equals('m') && spouse != null){
                addEvents(spouse);
            }
        }
        else {
            for(Person person : fatherSideMales){
                removeEvents(person);
            }
            for(Person person : motherSideMales){
                removeEvents(person);
            }
            if(user.getGender().equals('m')){
                removeEvents(user);
            }
            if(spouse.getGender().equals('m') && spouse != null){
                removeEvents(spouse);
            }
        }
    }

    public void femaleEventFilter(boolean isChecked){
        if(isChecked){
            if(isMotherSide){
                for (Person person : motherSideFemales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : motherSideFemales) {
                    removeEvents(person);
                }
            }
            if(isFatherSide){
                for (Person person : fatherSideFemales) {
                    addEvents(person);
                }
            }
            else {
                for (Person person : fatherSideFemales) {
                    removeEvents(person);
                }
            }
            if(user.getGender().equals('f')){
                addEvents(user);
            }
            if(spouse.getGender().equals('f') && spouse != null){
                addEvents(spouse);
            }
        }
        else {
            for(Person person : motherSideFemales){
                removeEvents(person);
            }
            for(Person person : fatherSideFemales){
                removeEvents(person);
            }
            if(user.getGender().equals('f')){
                removeEvents(user);
            }
            if(spouse.getGender().equals('f') && spouse != null){
                removeEvents(spouse);
            }
        }
    }

    public void updateFilters(){
        fatherSideFilter(isFatherSide);
        motherSideFilter(isMotherSide);
        maleEventFilter(isMaleEvents);
        femaleEventFilter(isFemaleEvents);
    }

    public ArrayList<Person> getFamily(Person currentPerson){
        family.clear();
        if(currentPerson.getFatherID() != null){
            family.add(personLookup.get(currentPerson.getFatherID()));
        }
        if(currentPerson.getMotherID() != null){
            family.add(personLookup.get(currentPerson.getMotherID()));
        }
        if(currentPerson.getSpouseID() != null){
            family.add(personLookup.get(currentPerson.getSpouseID()));
        }
        getChildren(currentPerson.getPersonID());
        return family;
    }

    public void getChildren(String parentID){
        for(Map.Entry<String, Person> entry : personLookup.entrySet()){
            Person currentPerson = entry.getValue();
            if(currentPerson.getFatherID() != null && currentPerson.getFatherID().equals(parentID)){
                family.add(currentPerson);
            }
            else if(currentPerson.getMotherID() != null && currentPerson.getMotherID().equals(parentID)){
                family.add(currentPerson);
            }
        }
    }

    public ArrayList<Person> getSearchPeople(String searchText){
        ArrayList<Person> personArrayList = new ArrayList<>();

        for(Map.Entry<String, Person> entry : personLookup.entrySet()){
            Person currentPerson = entry.getValue();
            String fName = currentPerson.getFirstName().toLowerCase();
            String lName = currentPerson.getLastName().toLowerCase();
            if(fName.contains(searchText.toLowerCase()) || lName.contains(searchText.toLowerCase())){
                personArrayList.add(currentPerson);
            }
        }

        return personArrayList;
    }

    public ArrayList<Event> getSearchEvents(String searchText){
        ArrayList<Event> eventArrayList = new ArrayList<>();

        for(Map.Entry<String, Event> entry : filteredEvents.entrySet()){
            Event currentEvent = entry.getValue();
            String country = currentEvent.getCountry().toLowerCase();
            String city = currentEvent.getCity().toLowerCase();
            String eventType = currentEvent.getEventType().toLowerCase();
            String year = Integer.toString(currentEvent.getYear());
            if(country.contains(searchText.toLowerCase()) || city.contains(searchText.toLowerCase())
                    || eventType.contains(searchText.toLowerCase()) || year.contains(searchText)){
                eventArrayList.add(currentEvent);
            }
        }
        return eventArrayList;
    }

    public Person getSpouse() {
        return spouse;
    }

    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }
}
