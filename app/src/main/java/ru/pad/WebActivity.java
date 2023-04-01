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

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class WebActivity extends AppCompatActivity {

    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        MyWebViewClient webViewClient = new MyWebViewClient(this);
        webView.setWebViewClient(webViewClient);

        //String url = "https://psytests.org/luscher/8color-run.html";
        //String data = webViewClient.getCleanedHTMLPage(url);
        //webView.loadData(data, "text/html", "windows1251");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

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

        @Override
        public void onPageFinished(WebView webView, String url) {
            webView.loadUrl("javascript:(function(){document.getElementById('nac1').style.display = 'none';})()");
            webView.loadUrl("javascript:(function(){document.getElementById('hDr').style.display = 'none';})()");
            webView.loadUrl("javascript:(function(){document.getElementById('pageContent').style.display = 'none';})()");
        }

        public String getCleanedHTMLPage(String url) {
            Thread netThread = new Thread(new Runnable() {
                String data;
                Document doc;

                @Override
                public void run() {
                    try {
                        doc = Jsoup.connect(url).get();
                        for (Element element : doc.select("div.reel reel_F")) {
                            element.remove();
                        }
                        data = doc.text();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            netThread.start();

            return "";
        }
    }
}