package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import java.util.Objects;

public class CalendarViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView textViewMonthYear;
    private RecyclerView recyclerViewCalendar;
    private LocalDate selectedDate;

    private String sportsmanUid, psychologistUid, role;

    private DatabaseReference dbUser;

    private final LocalDate localDateNow = LocalDate.now();

    private final int markSizeInDP = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        getUserData();
        initWidgets();
        selectedDate = localDateNow;
        setCalendarCells(localDateNow.getMonth().toString() + localDateNow.getYear());
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            psychologistUid = extras.getString("psychologistUid");
            role = extras.getString("role");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    @SuppressLint("NonConstantResourceId")
    private void initWidgets() {
        recyclerViewCalendar = findViewById(R.id.recyclerViewCalendar);
        textViewMonthYear = findViewById(R.id.textViewMonthYear);

        if (role.equals("Спортсмен")) {
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setVisibility(View.VISIBLE);
            bottomNavigationView.setSelectedItemId(R.id.calendar);
            bottomNavigationView.setOnItemSelectedListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.calendar:
                        return true;
                    case R.id.notes:
                        Intent noteWritingActivity = new Intent(CalendarViewActivity.this, NoteWritingActivity.class);
                        noteWritingActivity.putExtra("sportsmanUid", sportsmanUid);
                        startActivity(noteWritingActivity);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                    case R.id.profile:
                        Intent sportsmanProfileActivity = new Intent(CalendarViewActivity.this, SportsmanProfileActivity.class);
                        sportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
                        startActivity(sportsmanProfileActivity);
                        overridePendingTransition(0, 0);
                        finish();
                        return true;
                }
                return false;
            });
        }

        Button buttonPreviousMonth = findViewById(R.id.buttonPreviousMonth);
        buttonPreviousMonth.setOnClickListener(unused1 -> previousMonthAction());
        Button buttonNextMonth = findViewById(R.id.buttonNextMonth);
        buttonNextMonth.setOnClickListener(unused2 -> nextMonthAction());
    }

    private void setCalendarCells(String nextMonthYear) {
        textViewMonthYear.setText(getMonthYearFromDate(selectedDate));

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
        for (int i = 2; i <= 42; i++) {
            int[] marksInDay = {0, 0, 0, 0, 0};
            if (i > dayOfWeek && i <= monthLength + dayOfWeek) {
                String day = String.valueOf(i - dayOfWeek);
                if (day.length() == 1) {
                    day = "0" + day;
                }
                DatabaseReference activity = dbUser.child("activity").child(day + selectedDate.format(formatter));
                int finalI = i - 2;
                activity.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild("availableTests")) {
                            marksInDay[0] = markSizeInDP;
                        }
                        if (snapshot.hasChild("completedTests")) {
                            marksInDay[1] = markSizeInDP;
                        }
                        if (snapshot.hasChild("notCompletedTests")) {
                            marksInDay[2] = markSizeInDP;
                        }
                        if (snapshot.hasChild("assignedTests")) {
                            marksInDay[3] = markSizeInDP;
                        }
                        if (snapshot.hasChild("notes")) {
                            marksInDay[4] = markSizeInDP;
                        }

                        // marksInMonth.set(Integer.parseInt(Objects.requireNonNull(snapshot.getKey()).split("-")[0]), marksInDay);
                        marksInMonth.set(finalI, marksInDay);
                        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, marksInMonth,CalendarViewActivity.this, nextMonthYear);
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

    private ArrayList<String> getDaysOfMonthArray(LocalDate date) {
        ArrayList<String> daysOfMonth = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
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
        setCalendarCells(selectedDate.getMonth().toString() + selectedDate.getYear());
    }

    public void nextMonthAction() {
        selectedDate = selectedDate.plusMonths(1);
        setCalendarCells(selectedDate.getMonth().toString() + selectedDate.getYear());
    }

    @Override
    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            Intent selectedDayViewActivity = new Intent(CalendarViewActivity.this, SelectedDayViewActivity.class);
            selectedDayViewActivity.putExtra("sportsmanUid", sportsmanUid);
            selectedDayViewActivity.putExtra("psychologistUid", psychologistUid);
            selectedDayViewActivity.putExtra("role", role);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(".MM.yyyy");
            if (dayText.length() == 1) {
                dayText = "0" + dayText;
            }
            selectedDayViewActivity.putExtra("selectedDayDate", dayText + selectedDate.format(formatter));
            startActivity(selectedDayViewActivity);
            overridePendingTransition(0, 0);
        }
    }
}
