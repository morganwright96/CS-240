package com.example.shared.Result;

import com.example.shared.Model.Person;

import java.util.ArrayList;

/**
 * This class is the result of getting all the people for the current user
 */

public class PeopleResult extends Result{
    private ArrayList<Person> data = new ArrayList<>();

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }
}
