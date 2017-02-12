package org.softeg.morphinebrowser.pageviewcontrol;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.softeg.morphinebrowser.bus.FastEvent;

import static org.softeg.morphinebrowser.Constants.TOOLBAR_TITLE;


/*
 * Created by slinkin on 02.10.2014.
 */
class AppWebViewClient extends WebViewClient {
    private IWebViewClientListener listener;

    AppWebViewClient(IWebViewClientListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        FastEvent.emit(TOOLBAR_TITLE, "Загрузка...");
//        listener.setSupportProgressBarIndeterminateVisibility(true);
        //ThemeActivity.this.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        FastEvent.emit(TOOLBAR_TITLE, view.getTitle());
//        if (!TextUtils.isEmpty(view.getTitle())) {
//            listener.setPageTitle(view.getTitle(), url);
//        }
    }
/*
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, final String url) {

        try {
            Intent intent = IntentUtils.openLink(url);
            listener.getContext().startActivity(intent);
        } catch (Throwable ex) {
            AppLog.e(ex);
        }
        return true;
    }
    */

}
