package edu.wmich.petpatrol;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//singleton for all of the pet event objects.
public class Events {

    private static Events sEvents;

    private ArrayList<Event> mEvents;

    public static Events get(Context context) {
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
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public Event getEvent(UUID id) {
        for(Event event: mEvents) {
            if(event.getId().equals(id)) {
                return event;
            }
        }
        return null;
    }
}
