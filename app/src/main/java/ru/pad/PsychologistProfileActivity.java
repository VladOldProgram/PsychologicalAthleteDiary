package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PsychologistProfileActivity extends AppCompatActivity {
    Button buttonRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);

        buttonRequests = findViewById(R.id.buttonRequests);
        buttonRequests.setOnClickListener(view -> {
            Intent psychologistRequestsActivity = new Intent(this, PsychologistRequestsActivity.class);
            startActivity(psychologistRequestsActivity);
        });
    }
}
