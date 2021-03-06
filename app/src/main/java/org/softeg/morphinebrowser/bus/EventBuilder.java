package org.softeg.morphinebrowser.bus;

import android.app.Activity;

@SuppressWarnings("unused")
public class EventBuilder {

    private Event event;
    private FastEvent instance;

    EventBuilder(String event, FastEvent instance) {
        this.event = new Event();
        this.event.setEvent(event);
        this.instance = instance;
    }

    /**
     * Runnable to run on event
     */
    public void execute(EventCallback callback) {
        this.event.setCallback(callback);
        instance.register(event);
    }

    /**
     * Run event runnable inside a different thread
     */
    public EventBuilder async() {
        this.event.setAsync(true);
        return this;
    }

    /**
     * Use max priority in thread execution
     */
    public EventBuilder maxPriority() {
        this.event.setPriority(Thread.MAX_PRIORITY);
        return this;
    }

    /**
     * Use min priority in thread execution
     */
    public EventBuilder minPriority() {
        this.event.setPriority(Thread.MIN_PRIORITY);
        return this;
    }

    /**
     * Run runnable inside Main Thread UI
     */
    public EventBuilder onUi(Activity activity) {
        this.event.setActivity(activity);
        this.event.setOnUi(true);
        return this;
    }

}