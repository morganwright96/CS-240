package Service.Result;

import Model.Person;

import java.util.ArrayList;

/**
 * This class is the result of getting all the people for the current user
 */

public class PeopleResult {
    private ArrayList<Person> peopleList = new ArrayList<>();

    public ArrayList<Person> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(ArrayList<Person> peopleList) {
        this.peopleList = peopleList;
    }
}
