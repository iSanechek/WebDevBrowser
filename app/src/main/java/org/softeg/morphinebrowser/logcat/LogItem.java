package org.softeg.morphinebrowser.logcat;

import java.util.Date;

public interface LogItem {
    public Priority getPriority();

    public String getTag();

    public Date getDate();

    public String getMessage();

    public void setMessage(String message);

    public int getPid();
}