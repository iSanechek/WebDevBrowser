package org.softeg.morphinebrowser.logcat;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LogcatHandler implements LogHandler, Runnable {
    private final Context mContext;
    private final LogItemFactory mLogItemFactory;
    private final ArrayList<LogItem> mLogItems = new ArrayList<LogItem>();
    private final List<OnLogItemReadListener> mListeners = new ArrayList<OnLogItemReadListener>();
    private Thread mThread;

    public LogcatHandler(Context context, LogItemFactory logItemFactory) {
        mContext = context;
        mLogItemFactory = logItemFactory;
    }

    public void listen() throws IOException {
        if(mThread != null) {
            mThread.interrupt();
        }

        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public synchronized List<LogItem> getReadItems() {
        return new ArrayList<LogItem>(mLogItems);
    }

    @Override
    public void addOnLogItemReadListener(OnLogItemReadListener listener) {
        synchronized (mListeners) {
            mListeners.add(listener);
        }
    }

    @Override
    public void removeOnLogItemReadListener(OnLogItemReadListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    /**
     * Notifies listeners on the main thread.
     * @param item the log item that was read.
     */
    private void notifyListeners(final LogItem item) {
        Handler handler = new Handler(mContext.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (mListeners) {
                    for(OnLogItemReadListener listener : mListeners) {
                        if(listener != null) {
                            listener.onLogItemRead(item);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void run() {
        String line;
        try {
            Process process = Runtime.getRuntime().exec("logcat -v time");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            synchronized (mLogItems) {
                mLogItems.clear();
            }

            LogItem lastItem = null;

            while ((line = bufferedReader.readLine()) != null){
                if(line.startsWith("---------"))
                    continue;

                LogItem item = mLogItemFactory.create(line);

                if(lastItem != null && isItemEqualToOtherItemExceptMessage(item, lastItem)) {
                    lastItem.setMessage(lastItem.getMessage() + "\n" + item.getMessage());

                    notifyListeners(lastItem);
                }
                else {
                    synchronized (mLogItems) {
                        mLogItems.add(item);
                    }

                    notifyListeners(item);
                    lastItem = item;
                }

                if(Thread.interrupted()) {
                    break;
                }
            }
        } catch (IOException e) {
            Log.e("LogcatHandler", "Error reading org.softeg.morphinebrowser.logcat", e);
        } catch (ParseException e) {
            Log.e("LogcatHandler", "Parsing error when reading org.softeg.morphinebrowser.logcat", e);
        }
    }

    private boolean isItemEqualToOtherItemExceptMessage(LogItem item, LogItem other) {
        return item != null && other != null && other.getDate().equals(item.getDate()) &&
                other.getTag().equals(item.getTag()) &&
                other.getPid() == item.getPid() &&
                other.getPriority().equals(item.getPriority());
    }
}
