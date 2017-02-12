package org.softeg.morphinebrowser.logcat;

import java.util.List;

public interface LogHandler {
    /**
     * Get all log items that have been read until now.
     *
     * @return list of log items
     */
    public List<LogItem> getReadItems();

    /**
     * Adds a listener for when a new log item have been read
     * @param listener the listener that will be notified
     */
    public void addOnLogItemReadListener(OnLogItemReadListener listener);

    /**
     * Removes a listener for incoming log items. Should be done in onDestroy etc.
     * @param listener the listener to be removed
     */
    public void removeOnLogItemReadListener(OnLogItemReadListener listener);

    /**
     * Interface for listening on incoming log items
     */
    public interface OnLogItemReadListener {
        /**
         * Callback for new log items read from the log
         *
         * @param item the log item that was read
         */
        public void onLogItemRead(LogItem item);
    }
}
