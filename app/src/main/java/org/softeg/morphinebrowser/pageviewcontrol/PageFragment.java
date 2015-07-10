package org.softeg.morphinebrowser.pageviewcontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import org.softeg.morphinebrowser.AppLog;
import org.softeg.morphinebrowser.AppPreferences;
import org.softeg.morphinebrowser.R;
import org.softeg.morphinebrowser.common.UrlExtensions;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.Developer;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.HtmlOutManager;
import org.softeg.morphinebrowser.pageviewcontrol.htmloutinterfaces.IHtmlOutListener;

import java.io.File;
import java.util.ArrayList;

/*
 * Created by slinkin on 01.10.2014.
 */
public abstract class PageFragment extends PageViewFragment implements
        IHtmlOutListener {
    public static final String ID = "org.softeg.slartus.car72.topic.PageFragment";
    public static final String SCROLL_Y_KEY = "PageFragment.SCROLL_Y_KEY";
    private static final String TAG = "PageFragment";

    protected HtmlOutManager mHtmlOutManager;
    protected int mScrollY = 0;

    protected void initHtmlOutManager(){
        mHtmlOutManager = new HtmlOutManager(this);
        mHtmlOutManager.registerInterfaces(mWebView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initHtmlOutManager();


        if (savedInstanceState != null && savedInstanceState.containsKey(SCROLL_Y_KEY)) {
            mScrollY = savedInstanceState.getInt(SCROLL_Y_KEY);
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
        switch (hitTestResult.getType()) {
            case WebView.HitTestResult.UNKNOWN_TYPE:
            case WebView.HitTestResult.EDIT_TEXT_TYPE:
                break;
            default: {
                UrlExtensions.showChoiceDialog(getActivity(), hitTestResult.getExtra());
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_Y_KEY, mWebView.getScrollY());
    }


    protected void setLoading(boolean loading) {
        try {
            if (getActivity() == null) return;


        } catch (Throwable ignore) {

            android.util.Log.e("TAG", ignore.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHtmlOutManager.onActivityResult(requestCode, resultCode, data);
    }



    public void saveHtml() {
        try {
            mWebView.evalJs("window." + Developer.NAME + ".saveHtml('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        } catch (Throwable ex) {
            AppLog.e(getActivity(), ex);
        }
    }


    protected void showFontSizeDialog() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.font_size_dialog, null);

        assert v != null;
        final SeekBar seekBar = (SeekBar) v.findViewById(R.id.value_seekbar);
        seekBar.setProgress(AppPreferences.getWebViewFontSize());
        final TextView textView = (TextView) v.findViewById(R.id.value_textview);
        textView.setText((seekBar.getProgress() + 1) + "");

        v.findViewById(R.id.button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 1){
                    int i=seekBar.getProgress() - 1;

                    seekBar.setProgress(i);
                    getWebView().getSettings().setDefaultFontSize(i + 1);
                    textView.setText((i + 1) + "");
                }
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < seekBar.getMax()) {
                    int i=seekBar.getProgress() + 1;

                    seekBar.setProgress(i);
                    getWebView().getSettings().setDefaultFontSize(i + 1);
                    textView.setText((i + 1) + "");
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                getWebView().getSettings().setDefaultFontSize(i + 1);
                textView.setText((i + 1) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        new AlertDialog.Builder(getActivity())
                .setTitle("Размер шрифта")
                .setView(v)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        AppPreferences.setWebViewFontSize(seekBar.getProgress() + 1);
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        getWebView().getSettings().setDefaultFontSize(AppPreferences.getWebViewFontSize());
                    }
                })
                .create().show();

    }

    protected void showSelectStyleDialog(){
//        try {
//            Context context = getActivity();
//            ArrayList<CharSequence> newStyleNames = new ArrayList<CharSequence>();
//            newStyleNames.add("Стандартный");
//            newStyleNames.add("Режим разработчика");
//            final ArrayList<CharSequence> newstyleValues = new ArrayList<CharSequence>();
//            newstyleValues.add(AppAssetsManager.getAssetsPath()+"forum/css/style.css");
//            newstyleValues.add(TopicApi.CSS_DEVELOPER);
//            File file = new File(context.getExternalFilesDir(null).getPath() + File.separator + "css");
//            getStylesList(newStyleNames, newstyleValues, file, ".css");
//
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("Выбор стиля")
//                    .setSingleChoiceItems(
//                            newStyleNames.toArray(new CharSequence[newStyleNames.size()]),
//                            newstyleValues.indexOf(AppPreferences.Topic.getStyleCssPath()), new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                    AppPreferences.Topic.setStyleCssPath(newstyleValues.get(i).toString());
//                                    refreshPage();
//                                }
//                            })
//                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//                        }
//                    }).create().show();
//        } catch (Throwable ex) {
//            AppLog.e(ex);
//        }
    }


    private static void getStylesList(ArrayList<CharSequence> newStyleNames, ArrayList<CharSequence> newstyleValues,
                                      File file, String ext) {

        if (file.exists()) {
            File[] cssFiles = file.listFiles();
            assert cssFiles != null;
            for (File cssFile : cssFiles) {
                if (cssFile.isDirectory()) {
                    getStylesList(newStyleNames, newstyleValues, cssFile, ext);
                    continue;
                }
                String cssPath = cssFile.getPath();
                if (!cssPath.toLowerCase().endsWith(ext)) continue;

                newStyleNames.add(cssFile.getName());
                newstyleValues.add(cssPath);

            }
        }
    }
}
