package org.softeg.morphinebrowser;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*
 * Created by slartus on 25.10.2014.
 */
public class AppPreferences {
    public static int getWebViewFontSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return prefs.getInt("WebView.FontSize", 15);
    }

    public static void setWebViewFontSize(int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        prefs.edit().putInt("WebView.FontSize", value).apply();
    }

    public static int getCacheMode() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return prefs.getInt("WebView.CacheMode", 1);
    }

    public static void setCacheMode(int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        prefs.edit().putInt("WebView.CacheMode", value).apply();
    }

    public static void firstStartDone() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        prefs.edit().putBoolean("First.Start", false).apply();
    }

    public static boolean isFirstStart() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return prefs.getBoolean("First.Start", true);
    }

    public static void saveLastLink(String url) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        prefs.edit().putString("Save.Url", url).apply();
    }

    public static String getLastLink() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return prefs.getString("Save.Url", "");
    }

}
