package Model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * This is a basic event class
 * This class represents a given event for a person
 * A person can have more than one event linked to them
 */
public class Event {
    private String eventID = "";
    @SerializedName("associatedUsername")
    private String userName = "";
    private String personID = "";
    private float latitude = 0;
    private float longitude = 0;
    private String country = "";
    private String city = "";
    private String eventType = "";
    private int year = 0;

    /**
     * This creates an instance of an event with the given params
     * @param eventID The id for the event
     * @param userName The username for the current user
     * @param personID The id of the person that is linked to the event
     * @param latitude The latitude of the event
     * @param longitude The longitude of the event
     * @param country The country the event occurred in
     * @param city The city the event occurred in
     * @param eventType The type of event baptism, christening, death, birth, etc.
     * @param year The year the event occured
     */

    public Event(String eventID, String userName, String personID, float latitude,
                 float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.userName = userName;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country.replaceAll("\"", "");
        this.city = city.replaceAll("\"", "");
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.latitude, latitude) == 0 &&
                Double.compare(event.longitude, longitude) == 0 &&
                year == event.year &&
                Objects.equals(eventID, event.eventID) &&
                Objects.equals(userName, event.userName) &&
                Objects.equals(personID, event.personID) &&
                Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) &&
                Objects.equals(eventType, event.eventType);
    }
}
