package org.softeg.morphinebrowser;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.transitionseverywhere.TransitionManager;

import org.softeg.morphinebrowser.bus.Args;
import org.softeg.morphinebrowser.bus.EventCallback;
import org.softeg.morphinebrowser.bus.FastEvent;
import org.softeg.morphinebrowser.controls.AppWebView;
import org.softeg.morphinebrowser.logcat.LogFragment;


public class MainActivity extends AppCompatActivity {

    private TextView toolbar_title;
    private LogFragment logFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.e("TEST", "onCreate");

        setContentView(R.layout.activity_main);
        // Инициализирую Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.app_name);
        toolbar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, toolbar_title.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        String url = null;
        if (getIntent() != null) {
            url = getIntent().getDataString();
            Log.e("Main Activity", "onCreate: " + url);
        }

        Log.e("Main Activity", "onCreate: " + url);
        createFragment(url);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeBus();
    }

    private void initializeBus() {
        FastEvent.on(Constants.SHOW_LOG_FRAGMENT)
                .onUi(MainActivity.this)
                .execute(new EventCallback() {
                    @Override
                    public void onEvent(Args args) {
                        ViewGroup group = (ViewGroup) findViewById(R.id.root_layout);
                        TransitionManager.beginDelayedTransition(group);
                        View view = findViewById(R.id.log_fragment_container);
                        view.setVisibility(args.get(0) ? View.VISIBLE : View.GONE);
                        if (args.get(0)) {
                            findViewById(R.id.bord).setVisibility(View.VISIBLE);
                            view.setVisibility(View.VISIBLE);
                            logFragment = new LogFragment();
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.log_fragment_container, logFragment)
                                    .commit();
                        } else {
                            view.setVisibility(View.GONE);
                            findViewById(R.id.bord).setVisibility(View.GONE);
                            if (logFragment != null) {
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .remove(logFragment)
                                        .commit();
                            }
                        }
                    }
                });

        FastEvent.on(Constants.TOOLBAR_TITLE)
                .onUi(MainActivity.this)
                .execute(new EventCallback() {
                    @Override
                    public void onEvent(Args args) {
                        toolbar_title.setText(args.get(0).toString());
                    }
                });


    }

    private void createFragment(String uri) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topic_fragment_container, WebFragment.getInstance(uri), WebFragment.ID)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @Override
    public void onBackPressed() {

        AppWebView webView = ((WebFragment)getSupportFragmentManager().findFragmentById(R.id.topic_fragment_container)).getWebView();
        if (webView!=null && webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}