package ru.pad;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl("https://psytests.org/luscher/8color-run.html");

        MyWebViewClient webViewClient = new MyWebViewClient(this);
        webView.setWebViewClient(webViewClient);
    }

    //@Override
    //public boolean onKeyDown(int keyCode, KeyEvent event) {
    //    if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
    //        this.webView.goBack();
    //        return true;
    //    }
    //
    //    return super.onKeyDown(keyCode, event);
    //}

    public static class MyWebViewClient extends WebViewClient {

        private final Activity activity;

        public MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            // все ссылки, в которых содержится "psytests.org",
            // будут открываться внутри приложения
            if (url.contains("https://psytests.org")) {
                return false;
            }
            // для всех остальных ссылок будет спрашиваться, какой браузер открывать
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
    }
}