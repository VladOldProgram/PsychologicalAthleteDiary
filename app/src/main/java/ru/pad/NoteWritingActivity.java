package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.pad.objects.User;

public class NoteWritingActivity extends AppCompatActivity {
    private String uid;
    private User user;

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
            uid = extras.getString("uid");
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUser = database.getReference("Users/" + uid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void initWidgets() {
        SeekBar seekBarNoteWriting = findViewById(R.id.seekBarNoteWriting);
        TextView textViewRating = findViewById(R.id.textViewRating);

        seekBarNoteWriting.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                textViewRating.setText("Рейтинг: " + String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.notes);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    Intent calendarViewActivity = new Intent(getApplicationContext(), CalendarViewActivity.class);
                    calendarViewActivity.putExtra("uid", uid);
                    calendarViewActivity.putExtra("role", "Спортсмен");
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.notes:
                    return true;
                case R.id.profile:
                    Intent sportsmanProfileActivity = new Intent(this, SportsmanProfileActivity.class);
                    sportsmanProfileActivity.putExtra("uid", uid);
                    startActivity(sportsmanProfileActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });
    }
}
