package org.softeg.morphinebrowser;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import org.softeg.morphinebrowser.controls.AppWebView;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        // Инициализирую Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createFragment(getIntent() != null ? getIntent().getData() : null);
    }

    private void createFragment(Uri uri) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topic_fragment_container, WebFragment.getInstance(uri), WebFragment.ID)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {

        AppWebView webView = ((WebFragment)getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container)).getWebView();
           /* if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }*/
        if (webView!=null && webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    /**
     * Понять и простить
     */

    public void showProgress(boolean show) {
        if (show)
            toolbar.setTitle("Загрузка...");
        else
            clearTitle(null);
    }

    public void changeTitle(String text) {
        toolbar.setTitle(text);
    }

    public void clearTitle(String title) {
        if (title == null)
            toolbar.setTitle(R.string.app_name);
        else
            toolbar.setTitle(title);
    }
}