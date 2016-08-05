package org.softeg.morphinebrowser.pageviewcontrol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
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

    protected void initHtmlOutManager() {
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
        changeText(getString(R.string.fontsize));

        if (AppPreferences.isFirstStart()) {
            showWarningDialog();
        }

        assert v != null;
        final DiscreteSeekBar sb = (DiscreteSeekBar) v.findViewById(R.id.value_seek_bar);
        sb.setProgress(AppPreferences.getWebViewFontSize());
        final EditText editText = (EditText) v.findViewById(R.id.value_text);
        editText.setText((sb.getProgress()) + "");

        v.findViewById(R.id.button_minus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.getProgress() > 0) {
                    int i = sb.getProgress() - 1;

                    sb.setProgress(i);
                    getWebView().getSettings().setDefaultFontSize(i + 1);
                    editText.setText((i + 1) + "");
                }
            }
        });
        v.findViewById(R.id.button_plus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sb.getProgress() < sb.getMax()) {
                    int i = sb.getProgress() + 1;

                    sb.setProgress(i);
                    getWebView().getSettings().setDefaultFontSize(i + 1);
                    editText.setText((i + 1) + "");
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    sb.setProgress(Integer.valueOf(s.toString()) - 1);
                    editText.setSelection(s.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sb.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                getWebView().getSettings().setDefaultFontSize(value);
                editText.setText((value) + "");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });

        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(v);
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                clearText();
                AppPreferences.setWebViewFontSize(sb.getProgress());
            }
        });

        editText.selectAll();
        editText.requestFocus();

        changeFontSize(sb.getProgress());
//        ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    protected void showSelectStyleDialog() {
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

    protected void writeUrl() {
        View v = getActivity().getLayoutInflater().inflate(R.layout.url_textview, null);
        assert v != null;
        final EditText editText = (EditText) v.findViewById(R.id.editText);
        editText.setText(globalUrl);

        new MaterialDialog.Builder(getActivity())
                .title(R.string.enter_url)
                .customView(v, false)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        loadUrl(editText.getText().toString());
                        super.onPositive(dialog);
                    }
                })
                .show();
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

    protected void showCacheDialog() {
        new MaterialDialog.Builder(getContext())
                .title(R.string.cache_settings)
                .items(new String[]{getString(R.string.c_s), getString(R.string.off_c), getString(R.string.only_c)})
                .itemsCallbackSingleChoice(AppPreferences.getCacheMode() - 1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                        AppPreferences.setCacheMode(i + 1);
                        return false;
                    }
                })
                .show();
    }

    protected void showWarningDialog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(getActivity())
                        .title("Предупреждение")
                        .content("Для закрытия диалога регулеровки свайпните вниз")
                        .positiveText("Я понял(а)")
                        .canceledOnTouchOutside(false)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                AppPreferences.firstStartDone();
                            }
                        }).show();
            }
        }, 500);
    }
}