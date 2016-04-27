package edu.wmich.petpatrol;


/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This is an object that contains
 * information about an Event
*************************************
*/

import android.location.Location;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

// individual events.
public class Event {

    private UUID mEventId;  /* Unique Identifier for Event */
    private String mEventName; /* Name of the event */
    private Calendar mEventStartDateTime;   /* Date & Time the event starts */
    private Calendar mEventEndDateTime; /* Date & Time the event ends */
    private Location mLocation; /* Coordinate location of where the event will take place */
    private int mContactNumber; /* Phone number the event creator can be reached at */
    private String mDetails; /* Additional event notes */
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

    public Event() {    /* Creates a new event object with a random ID and new calendar objects */
        mEventId = UUID.randomUUID();
        mEventStartDateTime = Calendar.getInstance();
        mEventEndDateTime = Calendar.getInstance();
    }

    public UUID getId() {   /* Returns the event ID */
        return mEventId;
    }

    public void setEventId(UUID eventId) {  /* Sets the event ID */
        this.mEventId = eventId;
    }

    public String getEventName() {  /* Returns the event name */
        return mEventName;
    }

    public void setEventName(String eventName) {    /* Sets the event name */
        this.mEventName = eventName;
    }

    public Calendar getEventStartDateTime() {   /* Returns the event start date and time */
        return mEventStartDateTime;
    }

    public void setEventStartDateTime(Calendar eventStartDateTime) {    /* Sets the event start date and time */
        this.mEventStartDateTime = eventStartDateTime;
    }

    public Calendar getEventEndDateTime() { /* Returns the event end date and time */
        return mEventEndDateTime;
    }

    public void setEventEndDateTime(Calendar eventEndDateTime) {    /* Sets the event start date and time */
        this.mEventEndDateTime = eventEndDateTime;
    }

    public int getContactNumber() { /* Returns the event contact number */
        return mContactNumber;
    }

    public void setContactNumber(int contactNumber) {   /* Sets the event contact number */
        this.mContactNumber = contactNumber;
    }

    public String getDetails() {    /* Returns the event details */
        return mDetails;
    }

    public void setDetails(String details) {    /* Sets the event details */
        this.mDetails = details;
    }

    public Location getLocation() { return mLocation; } /* Returns the event location */

    public void setLocation(Location location) { this.mLocation = location; }   /* Sets the event location */
}
