package org.softeg.morphinebrowser.logcat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LogcatTextFactory implements LogTextFactory {
    @Override
    public String create(List<LogItem> items) {

        StringBuilder builder = new StringBuilder();
        builder.append("Steps to reproduce:\n\n");
        builder.append("What happened?\n\n");
        builder.append("What should happen?\n\n");

        builder.append("Error log:\n\n");

        for(LogItem logItem : items) {
            builder.append(getDateString(logItem.getDate())).append(" ")
                    .append(logItem.getPriority().toString()).append("/")
                    .append(logItem.getTag()).append("(").append(logItem.getPid()).append("): ")
                    .append(logItem.getMessage()).append("\n");
        }

        return builder.toString();
    }

    public String getDateString(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return String.format("%02d-%02d %02d:%02d:%02d.%03d",
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                cal.get(Calendar.MILLISECOND));
    }
}
