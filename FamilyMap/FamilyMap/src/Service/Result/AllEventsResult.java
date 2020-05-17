package Service.Result;

import Model.Event;

import java.util.ArrayList;

/**
 * This class is the result of getting all the events based on a user
 */
public class AllEventsResult extends Result {
    private ArrayList<Event> eventList = new ArrayList<>();

    public ArrayList<Event> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<Event> eventList) {
        this.eventList = eventList;
    }
}
