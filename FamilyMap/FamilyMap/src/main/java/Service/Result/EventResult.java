package Service.Result;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * This is the response of getting a single event from the database
 */
public class EventResult extends Result {
    @SerializedName("eventID")
    private String eventID = null;
    @SerializedName("associatedUsername")
    private String username = null;
    @SerializedName("personID")
    private String personID = null;
    @SerializedName("latitude")
    private double latitude = 0;
    @SerializedName("longitude")
    private double longitude = 0;
    @SerializedName("country")
    private String country = null;
    @SerializedName("city")
    private String city = null;
    @SerializedName("eventType")
    private String eventType = null;
    @SerializedName("year")
    private int year = 0;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

}
