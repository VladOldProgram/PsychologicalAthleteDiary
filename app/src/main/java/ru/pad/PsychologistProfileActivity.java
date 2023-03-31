package ru.pad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

/* Файл профиля психолога */
public class PsychologistProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);
    }

    public void startRequestsActivity (View v) {
        Intent intent = new Intent(PsychologistProfileActivity.this, PsychologistRequestsActivity.class);
        startActivity(intent);
    }
}