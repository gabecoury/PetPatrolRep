package edu.wmich.petpatrol;

import android.location.Location;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

// individual events.
public class Event {

    private UUID mEventId;
    private String mEventName;
    private Calendar mEventStartDateTime;
    private Calendar mEventEndDateTime;
    private Location mLocation;
    private int mContactNumber;
    private String mDetails;
    private double latit;
    private double longi;

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public double getLatit() {
        return latit;
    }

    public void setLatit(double latit) {
        this.latit = latit;
    }

    public Event() {
        mEventId = UUID.randomUUID();
        mEventStartDateTime = Calendar.getInstance();
        mEventEndDateTime = Calendar.getInstance();
    }

    public UUID getId() {
        return mEventId;
    }

    public void setEventId(UUID eventId) {
        this.mEventId = eventId;
    }

    public String getEventName() {
        return mEventName;
    }

    public void setEventName(String eventName) {
        this.mEventName = eventName;
    }

    public Calendar getEventStartDateTime() {
        return mEventStartDateTime;
    }

    public void setEventStartDateTime(Calendar eventStartDateTime) {
        this.mEventStartDateTime = eventStartDateTime;
    }

    public Calendar getEventEndDateTime() {
        return mEventEndDateTime;
    }

    public void setEventEndDateTime(Calendar eventEndDateTime) {
        this.mEventEndDateTime = eventEndDateTime;
    }

    public int getContactNumber() {
        return mContactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.mContactNumber = contactNumber;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        this.mDetails = details;
    }

    public Location getLocation() { return mLocation; }

    public void setLocation(Location location) { this.mLocation = location; }
}
