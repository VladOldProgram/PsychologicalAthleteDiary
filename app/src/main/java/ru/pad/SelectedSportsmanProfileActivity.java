package ru.pad;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SelectedSportsmanProfileActivity  extends AppCompatActivity {
    private String psychologistUid;
    private String sportsmanUid;

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

    }
}
