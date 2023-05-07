package ru.pad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Класс объекта активности календаря спортсмена
 */
public class CalendarViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView textViewMonthYear;
    private RecyclerView recyclerViewCalendar;

    private LocalDate selectedDate;

    private String sportsmanUid, role;

    private DatabaseReference dbUser;

    private final LocalDate localDateNow = LocalDate.now();

    private final int markSizeInDP = 50;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        getUserData();
        initWidgets();
        selectedDate = localDateNow;
        setCalendarCells();

        /*
          Если пользователь - спортсмен,
          то перегрузка метода обработки нажатия back: возвращение к активности профиля спортсмена
         */
        if (role.equals("Спортсмен")) {
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent sportsmanProfileActivity = new Intent(CalendarViewActivity.this, SportsmanProfileActivity.class);
                    sportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
                    startActivity(sportsmanProfileActivity);
                    overridePendingTransition(0, 0);
                    finish();
                }
            };
            getOnBackPressedDispatcher().addCallback(this, callback);
        }
    }

    /**
     * Сохраняет в текущем контексте данные, переданные из предыдущей активности
     */
    private void getUserData() {
        /*
          Получение данных, переданных из предыдущей активности
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            role = extras.getString("role");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    /**
     * Инициализирует виджеты активности
     */
    @SuppressLint("NonConstantResourceId")
    private void initWidgets() {
        /*
          Инициализация легенды календаря
         */
        Dialog calendarLegend = new Dialog(CalendarViewActivity.this);
        calendarLegend.setContentView(R.layout.calendar_legend);

        Window legendWindow = calendarLegend.getWindow();
        WindowManager.LayoutParams wlp = legendWindow.getAttributes();
        wlp.gravity = Gravity.TOP;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        legendWindow.setAttributes(wlp);

        Button buttonCloseLegend = calendarLegend.findViewById(R.id.buttonCloseLegend);
        buttonCloseLegend.setOnClickListener(v -> calendarLegend.dismiss());
        calendarLegend.setCancelable(false);

        Button buttonShowLegend = findViewById(R.id.buttonShowLegend);
        buttonShowLegend.setOnClickListener(unused1 -> {
            calendarLegend.show();
        });

        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        textViewMonthYear = findViewById(R.id.textViewMonthYear);

        /*
          Привязка к кнопкам функций переключения месяцев
         */
        Button buttonPreviousMonth = findViewById(R.id.buttonPreviousMonth);
        buttonPreviousMonth.setOnClickListener(unused2 -> previousMonthAction());
        Button buttonNextMonth = findViewById(R.id.buttonNextMonth);
        buttonNextMonth.setOnClickListener(unused3 -> nextMonthAction());

        /*
          Привязка к кнопкам меню функций перехода в выбранную активность
         */
        if (role.equals("Спортсмен")) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.calendar);
            bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.calendar:
                        return true;
                    case R.id.notes:
                        Intent noteWritingActivity = new Intent(CalendarViewActivity.this, NoteWritingActivity.class);
                        noteWritingActivity.putExtra("sportsmanUid", sportsmanUid);
                        noteWritingActivity.putExtra("role", "Спортсмен");
                        startActivity(noteWritingActivity);
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        finish();
                        return true;
                    case R.id.profile:
                        Intent sportsmanProfileActivity = new Intent(CalendarViewActivity.this, SportsmanProfileActivity.class);
                        sportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
                        startActivity(sportsmanProfileActivity);
                        overridePendingTransition(0, 0);
                        finishAffinity();
                        finish();
                        return true;
                }
                return false;
            });
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Заполняет ячейки календаря
     */
    private void setCalendarCells() {
        /*
          Отображение выбранного месяца и года
         */
        textViewMonthYear.setText(getMonthYearFromDate(selectedDate));

        /*
          Инициализация номеров и меток дней месяца
         */
        ArrayList<String> daysInMonth = getDaysOfMonthArray(selectedDate);
        ArrayList<int[]> marksInMonth = new ArrayList<>();
        for (int i = 2; i <= 42; i++) {
            marksInMonth.add(new int[]{0, 0, 0, 0, 0});
        }

        YearMonth yearMonth = YearMonth.from(selectedDate);
        int monthLength = yearMonth.lengthOfMonth();
        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-MM-yyyy");
        /*
          Заполнение всех дней месяца своими номерами и метками
          (цикл от 2 до 42 включительно, т.к. нужно отображать недели в формате "пн-вт-ср-чт-пт-сб-вс")
         */
        for (int i = 2; i <= 42; i++) {
            int[] marksInDay = {0, 0, 0, 0, 0};
            /*
              Если день входит в выбранный месяц, то подписать его номер
             */
            if (i > dayOfWeek && i <= monthLength + dayOfWeek) {
                String day = String.valueOf(i - dayOfWeek);
                if (day.length() == 1) {
                    day = "0" + day;
                }
                String date = day + selectedDate.format(formatter);
                DatabaseReference activity = dbUser.child("activity").child(date);
                int finalI = i - 2;
                int marksInDayLength = marksInDay.length;
                /*
                  Запрос к БД на получение меток дня этой итерации цикла
                 */
                activity.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DateTimeFormatter localFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        for (int i = 0; i < marksInDayLength; i++) {
                            marksInDay[i] = 0;
                        }
                        /*
                          Если есть доступные для прохождения тесты
                         */
                        if (snapshot.hasChild("availableTests")) {
                            /*
                              И если текущая дата совпадает с выбранным днем календаря,
                              то это желтая метка
                             */
                            if (LocalDate.now().isEqual(LocalDate.parse(date, localFormatter))) {
                                marksInDay[0] = markSizeInDP;
                            }
                            /*
                              Иначе если текущая дата позже выбранного дня календаря,
                              то это красная метка
                             */
                            else if (LocalDate.now().isAfter(LocalDate.parse(date, localFormatter))) {
                                marksInDay[2] = markSizeInDP;
                            }
                            /*
                              Иначе если текущая дата раньше выбранного дня календаря,
                              то это серая метка
                             */
                            else if (LocalDate.now().isBefore(LocalDate.parse(date, localFormatter))) {
                                marksInDay[3] = markSizeInDP;
                            }
                        }
                        /*
                          Если есть пройденные тесты, то это зеленая метка
                         */
                        if (snapshot.hasChild("completedTests")) {
                            marksInDay[1] = markSizeInDP;
                        }
                        /*
                          Если есть заметки, то это синяя метка
                         */
                        if (snapshot.hasChild("notes")) {
                            marksInDay[4] = markSizeInDP;
                        }

                        marksInMonth.set(finalI, marksInDay);
                        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, marksInMonth,CalendarViewActivity.this, selectedDate.getMonth().toString() + selectedDate.getYear());
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(CalendarViewActivity.this, 7);
                        recyclerViewCalendar.setLayoutManager(layoutManager);
                        recyclerViewCalendar.setAdapter(calendarAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }
        }
    }

    /**
     * Из месяца создает массив надписей-номеров его дней
     * @param date дата, из месяца которой нужно получить массив номеров дней
     * @return массив номеров дней
     */
    private ArrayList<String> getDaysOfMonthArray(LocalDate date) {
        ArrayList<String> daysOfMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = date.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 2; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysOfMonth.add("");
            } else {
                daysOfMonth.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysOfMonth;
    }

    /**
     * Извлекает из даты месяц и год в русской локализации
     * @param date дата, из которой нужно извлечь месяц и год
     * @return месяц и год в строке формата "название_месяца год"
     */
    private String getMonthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String[] formattedDate = date.format(formatter).split(" ");
        String month = formattedDate[0];
        switch (month) {
            case "января":
            case "January":
                month = "Январь";
                break;
            case "февраля":
            case "February":
                month = "Февраль";
                break;
            case "марта":
            case "March":
                month = "Март";
                break;
            case "апреля":
            case "April":
                month = "Апрель";
                break;
            case "мая":
            case "May":
                month = "Май";
                break;
            case "июня":
            case "June":
                month = "Июнь";
                break;
            case "июля":
            case "July":
                month = "Июль";
                break;
            case "августа":
            case "August":
                month = "Август";
                break;
            case "сентября":
            case "September":
                month = "Сентябрь";
                break;
            case "октября":
            case "October":
                month = "Октябрь";
                break;
            case "ноября":
            case "November":
                month = "Ноябрь";
                break;
            case "декабря":
            case "December":
                month = "Декабрь";
                break;
        }

        return month + " " + formattedDate[1];
    }

    /**
     * Переключает отображение в календаре на предыдущий месяц
     */
    public void previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1);
        setCalendarCells();
    }

    /**
     * Переключает отображение в календаре на следующий месяц
     */
    public void nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1);
        setCalendarCells();
    }

    /**
     * Реализует открытие пользователем активности выбранного в календаре дня
     * @param position позиция элемента в наборе элементов адаптера
     * @param dayText тест элемента в наборе элементов адаптера
     */
    @Override
    public void onItemClick(int position, String dayText) {
        /*
          Если нажата ячейка текущего месяца (то есть непустая),
          то старт активности выбранного дня
         */
        if (!dayText.equals("")) {
            Intent selectedDayViewActivity = new Intent(CalendarViewActivity.this, SelectedDayViewActivity.class);
            selectedDayViewActivity.putExtra("sportsmanUid", sportsmanUid);
            selectedDayViewActivity.putExtra("role", role);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("-MM-yyyy");
            if (dayText.length() == 1) {
                dayText = "0" + dayText;
            }
            selectedDayViewActivity.putExtra("selectedDayDate", dayText + selectedDate.format(formatter));
            startActivity(selectedDayViewActivity);
            overridePendingTransition(0, 0);
        }
    }
}
