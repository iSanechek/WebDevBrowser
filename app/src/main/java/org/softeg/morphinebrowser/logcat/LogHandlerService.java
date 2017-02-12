package org.softeg.morphinebrowser.logcat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class LogHandlerService extends Service {
    private LogcatHandler mLogHandler;
    private LogHandlerServiceBinder mBinder = new LogHandlerServiceBinder();

    @Override
    public void onCreate() {
        super.onCreate();

        mLogHandler = new LogcatHandler(this, new LogcatItemFactory());

        try {
            mLogHandler.listen();
        } catch (IOException e) {
            Log.e("LogHandlerService", "Error starting log handler thread", e);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LogHandlerServiceBinder extends Binder {
        public LogHandler getLogHandler() {
            return mLogHandler;
        }
    }
}
