package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SelectedSportsmanProfileActivity  extends AppCompatActivity {
    private String psychologistUid, sportsmanUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_sportsman_profile);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            psychologistUid = extras.getString("psychologistUid");
            sportsmanUid = extras.getString("sportsmanUid");
        }
    }

    private void initWidgets() {
        Button buttonSportsmanCalendar = findViewById(R.id.buttonSportsmanCalendar);
        buttonSportsmanCalendar.setOnClickListener(unused -> {
            Intent calendarViewActivity = new Intent(SelectedSportsmanProfileActivity.this, CalendarViewActivity.class);
            calendarViewActivity.putExtra("psychologistUid", psychologistUid);
            calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
            calendarViewActivity.putExtra("role", "Психолог");
            startActivity(calendarViewActivity);
            overridePendingTransition(0, 0);
        });
    }
}
