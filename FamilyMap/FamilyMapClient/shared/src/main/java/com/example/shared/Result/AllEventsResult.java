package com.example.shared.Result;

import com.example.shared.Model.Event;

import java.util.ArrayList;

/**
 * This class is the result of getting all the events based on a user
 */
public class AllEventsResult extends Result {
    private ArrayList<Event> data = new ArrayList<>();

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }
}
