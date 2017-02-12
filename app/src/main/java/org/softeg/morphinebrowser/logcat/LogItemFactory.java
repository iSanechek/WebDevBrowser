package org.softeg.morphinebrowser.logcat;

public interface LogItemFactory {
    public LogItem create(String line) throws ParseException;
}