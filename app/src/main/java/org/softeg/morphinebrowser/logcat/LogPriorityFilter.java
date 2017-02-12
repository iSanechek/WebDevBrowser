package org.softeg.morphinebrowser.logcat;

import java.util.ArrayList;
import java.util.List;

public class LogPriorityFilter implements LogFilter {
    private final LogHandler mLogHandler;

    public LogPriorityFilter(LogHandler logHandler) {
        mLogHandler = logHandler;
    }

    @Override
    public List<LogItem> getFilteredItems(Priority logLevel) {
        List<LogItem> items = mLogHandler.getReadItems();
        List<LogItem> filtered = new ArrayList<LogItem>();

        for(LogItem item : items) {
            if(isPriorityInCurrentLoglevel(item.getPriority(), logLevel)) {
                filtered.add(item);
            }
        }

        return filtered;
    }

    private boolean isPriorityInCurrentLoglevel(Priority priority, Priority logLevel) {
        switch (logLevel) {
            case VERBOSE:   return LogLevel.VERBOSE.contains(priority);
            case DEBUG:     return LogLevel.DEBUG.contains(priority);
            case INFO:      return LogLevel.INFO.contains(priority);
            case WARNING:   return LogLevel.WARNING.contains(priority);
            case ERROR:     return LogLevel.ERROR.contains(priority);
            case FATAL:     return LogLevel.ASSERT.contains(priority);
            default:        return false;
        }
    }
}