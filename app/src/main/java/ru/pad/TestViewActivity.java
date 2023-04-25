package ru.pad;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class TestViewActivity extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference userActivityTests;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);

        database = FirebaseDatabase.getInstance();
        String uid = "eKZrep4MvhdcHiBznZ1GFZoPEzz1";
        userActivityTests = database.getReference("Users/" + uid + "/activity/tests");

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        MyWebViewClient webViewClient = new MyWebViewClient(this);
        webView.setWebViewClient(webViewClient);
        //String url = "https://psytests.org/mmpi/mmilM.html";
        //String url = "https://psytests.org/accent/dtla.html";
        String url = "https://psytests.org/luscher/8color-run.html";
        //webView.loadUrl(url);
        String data = null;
        Document doc;
        try {
            doc = new GetCleanedHTMLPageTask().execute(url).get();
            data = doc.outerHtml();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        webView.loadDataWithBaseURL(url, data, "text/html; charset=UTF-8", null, url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public class MyWebViewClient extends WebViewClient {

        private final Activity activity;

        public MyWebViewClient(Activity activity) {
            this.activity = activity;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            String resultUrl;
            // все ссылки, в которых содержится "psytests.org",
            // будут открываться внутри приложения
            if (url.contains("https://psytests.org")) {
                String data = null;
                Document doc;
                try {
                    doc = new GetCleanedHTMLPageTask().execute(url).get();
                    data = doc.outerHtml();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (url.contains("/result?v=")) {
                    resultUrl = url;
                    String testName = doc.select("#hDt").text().split("/")[0];
                    String currentTime = LocalTime.now().toString();
                    int i = currentTime.lastIndexOf(":");
                    currentTime = currentTime.substring(0, i);
                    userActivityTests
                            .child(LocalDate.now().toString())
                            .child(testName + currentTime)
                            .setValue(resultUrl);
                }
                webView.loadDataWithBaseURL(url, data, "text/html; charset=UTF-8", null, url);
                return false;
            }
            // для всех остальных ссылок будет спрашиваться, какой браузер открывать
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(intent);
            return true;
        }
        /*
        @Override
        public void onPageFinished(WebView webView, String url) {
            webView.loadUrl(
                    "javascript:(function() { " +
                            "document.getElementsByClassName('reel reel_F')[0].remove(); " +
                            "document.querySelector('body > div:last-of-type').remove(); " +
                            "document.querySelector('body > div:last-of-type').remove(); " +
                            "document.querySelector('body > div:last-of-type').remove(); " +
                            "document.getElementById('nac1').remove(); " +
                            "document.getElementById('hDr').remove(); " +
                            "document.getElementById('hDrm').remove(); " +
                            "document.getElementsByTagName('footer')[0].remove(); " +
                    "})()"
            );
        }
        */
    }

    @SuppressWarnings("deprecation")
    private static class GetCleanedHTMLPageTask extends AsyncTask<String, String, Document> {

        @Override
        protected Document doInBackground(String... urls) {
            Document doc;
            try {
                doc = Jsoup.connect(urls[0]).get();
                for (Element element : doc.select(
                        "head>script:nth-of-type(2)," +
                                "head>script:nth-of-type(3)," +
                                "head>script:nth-of-type(4)," +
                                "head>script:nth-of-type(5)," +
                                "body>script:nth-of-type(2)," +
                                "body>script:nth-of-type(3)," +
                                ".reel," +
                                "#nac1," +
                                "#hDr," +
                                "#hDrm," +
                                "footer"
                )) {
                    element.remove();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return doc;
        }
    }
}