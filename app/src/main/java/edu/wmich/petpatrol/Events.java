package edu.wmich.petpatrol;

/*
*************************************
* Pet Patrol
* CIS 4700: Mobile Commerce Development
* Spring 2016
*************************************
* This is a singleton for holding a list
* of Event objects and manipulating them.
*************************************
*/

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Events {

    private static Events sEvents;

    private ArrayList<Event> mEvents;

    public static Events get(Context context) { /* Events Singleton */
        if (sEvents == null) {
            sEvents = new Events(context);
        }
        return sEvents;
    }

    private Events(Context context) {
        mEvents = new ArrayList<>();
    }

    public void addEvent(Event e){
        mEvents.add(e);
    }   /* Add new event to the array */

    public List<Event> getEvents() {    /* Get all Events array */
        return mEvents;
    }

    public Event getEvent(UUID id) {    /* Get a single event specified by ID */
        for(Event event: mEvents) {
            if(event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }
}
