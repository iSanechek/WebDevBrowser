package org.softeg.morphinebrowser;

/*
 * Created by slartus on 25.10.2014.
 */
public class AppPreferences {
    public static int getWebViewFontSize() {
        return App.getPreferences().getInt("WebView.FontSize", 16);
    }

    public static void setWebViewFontSize(int value) {
        App.getPreferences().edit().putInt("WebView.FontSize", value).apply();
    }

    public static int getPageWidthSize() {
        return App.getPreferences().getInt("WebView.PageWidth", 0);
    }

    public static void setPageWidthSize(int value) {
        App.getPreferences().edit().putInt("WebView.PageWidth", value).apply();
    }

    public static int getCacheMode() {
        return App.getPreferences().getInt("WebView.CacheMode", 1);
    }

    public static void setCacheMode(int value) {
        App.getPreferences().edit().putInt("WebView.CacheMode", value).apply();
    }

    public static void firstStartDone() {
        App.getPreferences().edit().putBoolean("First.Start", false).apply();
    }

    public static boolean isFirstStart() {
        return App.getPreferences().getBoolean("First.Start", true);
    }
}
