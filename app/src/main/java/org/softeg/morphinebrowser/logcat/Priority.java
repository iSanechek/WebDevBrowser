package org.softeg.morphinebrowser.logcat;

/**
 * Created by isanechek on 12.02.17.
 */

public enum  Priority {
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    FATAL,
    SILENT;

    @Override
    public String toString() {
        switch (this) {
            case VERBOSE:   return "V";
            case DEBUG:     return "D";
            case INFO:      return "I";
            case WARNING:   return "W";
            case ERROR:     return "E";
            case FATAL:     return "F";
            case SILENT:    return "S";
            default:        return "UNKNOWN";
        }
    }
}
