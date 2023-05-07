package ru.pad;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Класс объекта активности прохождения психологических тестов
 */
public class TestViewActivity extends AppCompatActivity {
    private String sportsmanUid, testName, testUrl, testType;

    private DatabaseReference dbUser;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_view);
        getUserData();
        initWidgets();
    }

    /**
     * Сохраняет в текущем контексте данные, переданные из предыдущей активности, сохраняет и обновляет данные пользователя
     */
    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            testName = extras.getString("testName");
            testUrl = extras.getString("testUrl");
            testType = extras.getString("testType");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    /**
     * Инициализирует виджеты активности
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("SetJavaScriptEnabled")
    private void initWidgets() {
        /*
          Создание и настройка встроенного браузера
         */
        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);

        /*
          Создание собственного обработчика запросов и ответов, привязка его к webView
         */
        CustomWebViewClient webViewClient = new CustomWebViewClient();
        webView.setWebViewClient(webViewClient);

        /*
          Получение очищенной веб-страницы и отображение ее на экране
         */
        String data;
        Document doc;
        try {
            doc = new getCleanedHTMLPageTask().execute(testUrl).get();
            data = doc.outerHtml();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        webView.loadDataWithBaseURL(testUrl, data, "text/html; charset=utf-8", null, testUrl);
    }

    /**
     * Класс объекта собственного обработчика запросов и ответов
     */
    public class CustomWebViewClient extends WebViewClient {

        public CustomWebViewClient() { }

        /**
         * Перегрузка метода загрузки веб-страницы для psytests.org
         * @param webView встроенный браузер, где отображать веб-страницу
         * @param url ссылка на веб-страницу
         * @return false, если не следует больше производить никаких действий с загрузкой веб-страницы, иначе - true
         */
        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            String testResultUrl;
            /*
              Если адрес содержит домен psytests.org
             */
            if (url.contains("https://psytests.org")) {
                String data;
                Document doc;
                /*
                  То получить очищенную веб-страницу
                 */
                try {
                    doc = new getCleanedHTMLPageTask().execute(url).get();
                    data = doc.outerHtml();
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                /*
                  Если адрес указывает на результат только что пройденного теста
                 */
                if (url.contains("/result?v=")
                        && testType.equals("availableTests")
                ) {
                    testResultUrl = url;

                    String currentTime = LocalTime.now().toString();
                    int i = currentTime.lastIndexOf(".");
                    currentTime = currentTime.substring(0, i);
                    /*
                      То запрос к БД на сохранение результата теста спортсмена
                     */
                    dbUser
                            .child("activity")
                            .child(LocalDate.now().format(formatter))
                            .child("completedTests")
                            .child(testName.substring(0, testName.length() - 8) + " " + currentTime)
                            .setValue(testResultUrl);
                    /*
                      запрос к БД на удаление этого теста из раздела доступных для прохождения
                     */
                    dbUser
                            .child("activity")
                            .child(LocalDate.now().format(formatter))
                            .child("availableTests")
                            .child(testName)
                            .removeValue();
                }

                /*
                  Отобразить веб-страницу на экране
                 */
                webView.loadDataWithBaseURL(url, data, "text/html; charset=utf-8", null, url);

                return false;
            }

            return true;
        }
    }

    /**
     * Класс объекта очистителя веб-страниц с psytests.org
     */
    @SuppressWarnings("deprecation")
    private static class getCleanedHTMLPageTask extends AsyncTask<String, String, Document> {

        /**
         *
         * @param urls адреса веб-страниц для очистки
         * @return очищенный html-документ
         */
        @Override
        protected Document doInBackground(String... urls) {
            Document doc;
            try {
                /*
                  Получение html-документа веб-страницы
                 */
                doc = Jsoup.connect(urls[0]).get();
                /*
                  Удаление лишних элементов из html-документа
                 */
                for (Element element : doc.select(
                                // Рекламные скрипты в head и body
                                "head>script:nth-of-type(2)," +
                                "head>script:nth-of-type(3)," +
                                "head>script:nth-of-type(4)," +
                                "head>script:nth-of-type(5)," +
                                "body>script:nth-of-type(2)," +
                                // Классы рекламных элементов страницы
                                ".reel," +
                                ".page_side," +
                                // Классы элементов страницы, не относящихся к прохождению тестов
                                ".testRunInfo," +
                                ".byTags," +
                                ".byTagsRes," +
                                ".qtlMeter," +
                                ".resAlso," +
                                // Элементы, не относящиеся к прохождению тестов
                                "#rtag," +
                                "header," +
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