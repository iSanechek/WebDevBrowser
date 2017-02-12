package org.softeg.morphinebrowser.logcat;

public class ParseException extends Exception {
    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable e) {
        super(message, e);
    }
}