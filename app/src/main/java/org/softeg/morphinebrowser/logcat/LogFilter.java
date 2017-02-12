package org.softeg.morphinebrowser.logcat;

import java.util.List;

public interface LogFilter {
    List<LogItem> getFilteredItems(Priority logLevel);
}