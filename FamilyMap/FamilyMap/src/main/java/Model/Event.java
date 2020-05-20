package Model;

/**
 * This is a basic event class
 * This class represents a given event for a person
 * A person can have more than one event linked to them
 */
public class Event {
    private String EventID = "";
    private String Username = "";
    private String PersonID = "";
    private double Latitude = 0;
    private double Longitude = 0;
    private String Country = "";
    private String City = "";
    private String EventType = "";
    private int Year = 0;

    /**
     * This creates an instance of an event with the given params
     * @param eventID The id for the event
     * @param username The username for the current user
     * @param personID The id of the person that is linked to the event
     * @param latitude The latitude of the event
     * @param longitude The longitude of the event
     * @param country The country the event occurred in
     * @param city The city the event occurred in
     * @param eventType The type of event baptism, christening, death, birth, etc.
     * @param year The year the event occured
     */

    public Event(String eventID, String username, String personID, double latitude,
                 double longitude, String country, String city, String eventType, int year) {
        EventID = eventID;
        Username = username;
        PersonID = personID;
        Latitude = latitude;
        Longitude = longitude;
        Country = country;
        City = city;
        EventType = eventType;
        Year = year;
    }

    public String getEventID() {
        return EventID;
    }

    public void setEventID(String eventID) {
        EventID = eventID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPersonID() {
        return PersonID;
    }

    public void setPersonID(String personID) {
        PersonID = personID;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String eventType) {
        EventType = eventType;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }
}
