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
import com.google.firebase.auth.FirebaseAuth;
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

import ru.pad.objects.User;

public class CalendarViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener {
    private TextView textViewMonthYear;
    private RecyclerView recyclerViewCalendar;
    private LocalDate selectedDate;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference dbUser;

    private String uid;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dbUser = database.getReference("Users/" + uid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

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
                    Intent noteWritingActivity = new Intent(getApplicationContext(), NoteWritingActivity.class);
                    noteWritingActivity.putExtra("uid", uid);
                    startActivity(noteWritingActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.profile:
                    if (user.getRole().equals("Спортсмен")) {
                        Intent sportsmanProfileActivity = new Intent(this, SportsmanProfileActivity.class);
                        sportsmanProfileActivity.putExtra("uid", uid);
                        startActivity(sportsmanProfileActivity);
                    } else {
                        Intent psychologistProfileActivity = new Intent(this, PsychologistProfileActivity.class);
                        psychologistProfileActivity.putExtra("uid", uid);
                        startActivity(psychologistProfileActivity);
                    }
                    overridePendingTransition(0, 0);
                    finish();
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
        if (date.equals(LocalDate.now())) {
            arrayTODELETE.set(20, new int[]{0, 0, 50, 0, 50});
            arrayTODELETE.set(21, new int[]{0, 50, 50, 0, 0});
            arrayTODELETE.set(22, new int[]{50, 50, 0, 0, 50});
            arrayTODELETE.set(23, new int[]{0, 0, 0, 50, 0});
            arrayTODELETE.set(24, new int[]{0, 0, 0, 50, 0});
            arrayTODELETE.set(26, new int[]{0, 0, 0, 50, 0});
        }

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
