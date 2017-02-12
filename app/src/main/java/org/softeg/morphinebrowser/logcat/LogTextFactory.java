package org.softeg.morphinebrowser.logcat;

import java.util.List;

public interface LogTextFactory {
    public String create(List<LogItem> items);
}