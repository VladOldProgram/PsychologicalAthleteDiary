package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ru.pad.objects.Note;

public class NoteWritingActivity extends AppCompatActivity {
    private String sportsmanUid;

    private DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_writing);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility", "NonConstantResourceId"})
    private void initWidgets() {
        SeekBar seekBarMood = findViewById(R.id.seekBarMood);
        TextView textViewMood = findViewById(R.id.textViewMood);
        textViewMood.setText("Настроение: 0/10");

        seekBarMood.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textViewMood.setText("Настроение: " + progress + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        EditText editTextSituation = findViewById(R.id.editTextSituation);
        EditText editTextThoughts = findViewById(R.id.editTextThoughts);
        EditText editTextEmotions = findViewById(R.id.editTextEmotions);
        EditText editTextReactions = findViewById(R.id.editTextReactions);

        editTextSituation.setMovementMethod(ScrollingMovementMethod.getInstance());
        editTextThoughts.setMovementMethod(ScrollingMovementMethod.getInstance());
        editTextEmotions.setMovementMethod(ScrollingMovementMethod.getInstance());
        editTextReactions.setMovementMethod(ScrollingMovementMethod.getInstance());

        editTextSituation.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        editTextThoughts.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        editTextEmotions.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        editTextReactions.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        Button buttonSaveNote = findViewById(R.id.buttonSaveNote);
        buttonSaveNote.setOnClickListener(unused1 -> {
            if (editTextSituation.getText().toString().equals("")
                    && editTextThoughts.getText().toString().equals("")
                    && editTextEmotions.getText().toString().equals("")
                    && editTextReactions.getText().toString().equals("")
            ) {
                Toast.makeText(
                        NoteWritingActivity.this,
                        "Нужно заполнить хотя бы одно поле",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            String currentTime = LocalTime.now().toString();
            int i = currentTime.lastIndexOf(".");
            currentTime = currentTime.substring(0, i);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            Note note = new Note(
                    seekBarMood.getProgress(),
                    editTextSituation.getText().toString(),
                    editTextThoughts.getText().toString(),
                    editTextEmotions.getText().toString(),
                    editTextReactions.getText().toString()
            );

            dbUser
                    .child("activity")
                    .child(LocalDate.now().format(formatter))
                    .child("notes")
                    .child("Заметка " + currentTime)
                    .setValue(note);

            seekBarMood.setProgress(0);
            textViewMood.setText("Настроение: 0/10");
            editTextSituation.setText("");
            editTextThoughts.setText("");
            editTextEmotions.setText("");
            editTextReactions.setText("");

            Toast.makeText(
                    NoteWritingActivity.this,
                    "Заметка сохранена",
                    Toast.LENGTH_SHORT
            ).show();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.notes);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    Intent calendarViewActivity = new Intent(NoteWritingActivity.this, CalendarViewActivity.class);
                    calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
                    calendarViewActivity.putExtra("role", "Спортсмен");
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.notes:
                    return true;
                case R.id.profile:
                    Intent sportsmanProfileActivity = new Intent(NoteWritingActivity.this, SportsmanProfileActivity.class);
                    sportsmanProfileActivity.putExtra("sportsmanUid", sportsmanUid);
                    startActivity(sportsmanProfileActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });
    }
}
