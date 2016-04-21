package edu.wmich.petpatrol;

import android.location.Location;

import java.util.Date;
import java.util.UUID;

// individual events.
public class Event {

    private UUID mEventId;
    private String mEventName;
    private Date mEventStartDateTime;
    private Date mEventEndDateTime;
    private Location mLocation;
    private int mContactNumber;
    private String mDetails;

    public Event() {
        mEventId = UUID.randomUUID();
        mEventStartDateTime = new Date();
        mEventEndDateTime = new Date();
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

    public Date getEventStartDateTime() {
        return mEventStartDateTime;
    }

    public void setEventStartDateTime(Date eventStartDateTime) {
        this.mEventStartDateTime = eventStartDateTime;
    }

    public Date getEventEndDateTime() {
        return mEventEndDateTime;
    }

    public void setEventEndDateTime(Date eventEndDateTime) {
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
