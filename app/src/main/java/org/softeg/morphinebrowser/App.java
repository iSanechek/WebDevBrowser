package org.softeg.morphinebrowser;

import android.app.Application;
import android.os.Environment;

import java.util.concurrent.atomic.AtomicInteger;

/*
 * Created by slartus on 25.10.2014.
 */
public class App extends Application {
    private static final String FOLDER_NAME = "DWDBHtml";

    private static App INSTANCE = null;

    public App() {
        INSTANCE = this;
    }

    public static App getInstance() {
        if (INSTANCE == null) {
            new App();
        }

        return INSTANCE;
    }

    private AtomicInteger m_AtomicInteger = new AtomicInteger();

    public int getUniqueIntValue() {
        return m_AtomicInteger.incrementAndGet();
    }

    public static String getHtmlFolder() {
        return Environment.getExternalStorageDirectory() + "/" + FOLDER_NAME;
    }

}
