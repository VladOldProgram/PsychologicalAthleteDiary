package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.pad.objects.Sportsman;

public class NoteWritingActivity extends AppCompatActivity {
    private TextView textViewMood;

    private String sportsmanUid;
    private Sportsman sportsman;

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
        DatabaseReference dbUser = database.getReference("Users/" + sportsmanUid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sportsman = dataSnapshot.getValue(Sportsman.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initWidgets() {
        SeekBar seekBarMood = findViewById(R.id.seekBarMood);
        textViewMood = findViewById(R.id.textViewMood);
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
