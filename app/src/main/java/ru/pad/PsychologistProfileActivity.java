package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

import java.util.Objects;

import ru.pad.objects.User;

public class PsychologistProfileActivity extends AppCompatActivity {
    TextView textViewPsychologistNameSurname, textViewPsychologistBirthDate;
    Button buttonPsychologistProfileExit, buttonRequests;

    String uid;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);

        textViewPsychologistNameSurname = findViewById(R.id.textViewPsychologistNameSurname);
        textViewPsychologistBirthDate = findViewById(R.id.textViewPsychologistBirthDate);
        buttonPsychologistProfileExit = findViewById(R.id.buttonPsychologistProfileExit);
        buttonRequests = findViewById(R.id.buttonRequests);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    Intent calendarViewActivity = new Intent(getApplicationContext(), CalendarViewActivity.class);
                    calendarViewActivity.putExtra("uid", uid);
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.notes:
                    Intent noteWritingActivity = new Intent(getApplicationContext(), NoteWritingActivity.class);
                    noteWritingActivity.putExtra("uid", uid);
                    startActivity(noteWritingActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.profile:
                    return true;
            }
            return false;
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dbUser = database.getReference("Users/" + uid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String psychologistNameSurname = Objects.requireNonNull(user).getName() + " " + user.getSurname();
                String psychologistBirthDate = user.getBirthDate();
                textViewPsychologistNameSurname.setText(psychologistNameSurname);
                textViewPsychologistBirthDate.setText(psychologistBirthDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        buttonPsychologistProfileExit.setOnClickListener(unused1 -> {
            auth.signOut();
            Intent AuthorizationActivity = new Intent(this, AuthorizationActivity.class);
            startActivity(AuthorizationActivity);
            finish();
        });

        buttonRequests.setOnClickListener(unused2 -> {
            Intent psychologistRequestsActivity = new Intent(this, PsychologistRequestsActivity.class);
            startActivity(psychologistRequestsActivity);
            finish();
        });
    }
}
