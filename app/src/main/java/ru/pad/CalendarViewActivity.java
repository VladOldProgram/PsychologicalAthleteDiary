package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView textViewMonthYear;
    private RecyclerView recyclerViewCalendar;
    private LocalDate selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        initWidgets();
        selectedDate = LocalDate.now();
        setTextViewMonthYear(LocalDate.now().getMonth().toString() + LocalDate.now().getYear());
    }

    private void initWidgets() {
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        textViewMonthYear = findViewById(R.id.textViewMonthYear);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.calendar);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    return true;
                case R.id.notes:
                    // TODO: NotesActivity.class
                    //startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                    //overridePendingTransition(0, 0);
                    return true;
                case R.id.profile:
                    // TODO: sportsman or psychologist?
                    //startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                    //overridePendingTransition(0, 0);
                    return true;
            }
            return false;
        });

        Button buttonPreviousMonth = findViewById(R.id.buttonPreviousMonth);
        buttonPreviousMonth.setOnClickListener(unused1 -> previousMonthAction());
        Button buttonNextMonth = findViewById(R.id.buttonNextMonth);
        buttonNextMonth.setOnClickListener(unused2 -> nextMonthAction());
    }

    private void setTextViewMonthYear(String nextMonthYear) {
        textViewMonthYear.setText(getMonthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = getDaysInMonthArray(selectedDate);
        ArrayList<int[]> marksInMonth = getMarksInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, marksInMonth,this, nextMonthYear);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        recyclerViewCalendar.setLayoutManager(layoutManager);
        recyclerViewCalendar.setAdapter(calendarAdapter);
    }

    private ArrayList<int[]> getMarksInMonthArray(LocalDate date) {
        ArrayList<int[]> arrayTODELETE = new ArrayList<>();
        for (int i = 2; i <= 42; i++) {
            arrayTODELETE.add(new int[] {0, 0, 0, 0, 0});
        }
        arrayTODELETE.set(18, new int[] {0, 0, 50, 0, 50});
        arrayTODELETE.set(19, new int[] {0, 50, 50, 0, 0});
        arrayTODELETE.set(20, new int[] {50, 50, 0, 0, 50});
        arrayTODELETE.set(21, new int[] {0, 0, 0, 50, 0});
        arrayTODELETE.set(22, new int[] {0, 0, 0, 50, 0});
        arrayTODELETE.set(24, new int[] {0, 0, 0, 50, 0});

        return arrayTODELETE;
    }

    private ArrayList<String> getDaysInMonthArray(LocalDate date) {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for (int i = 2; i <= 42; i++) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add("");
            }
            else {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }

        return daysInMonthArray;
    }

    private String getMonthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        String[] formattedDate = date.format(formatter).split(" ");
        String month = formattedDate[0];
        switch (month) {
            case "January":
                month = "Январь";
                break;
            case "February":
                month = "Февраль";
                break;
            case "March":
                month = "Март";
                break;
            case "April":
                month = "Апрель";
                break;
            case "May":
                month = "Май";
                break;
            case "June":
                month = "Июнь";
                break;
            case "July":
                month = "Июль";
                break;
            case "August":
                month = "Август";
                break;
            case "September":
                month = "Сентябрь";
                break;
            case "October":
                month = "Октябрь";
                break;
            case "November":
                month = "Ноябрь";
                break;
            case "December":
                month = "Декабрь";
                break;
        }

        return month + " " + formattedDate[1];
    }

    public void previousMonthAction() {
        selectedDate = selectedDate.minusMonths(1);
        setTextViewMonthYear(selectedDate.getMonth().toString() + selectedDate.getYear());
    }

    public void nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1);
        setTextViewMonthYear(selectedDate.getMonth().toString() + selectedDate.getYear());
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            // TODO: переход в окно текущего дня
        }
    }
}
