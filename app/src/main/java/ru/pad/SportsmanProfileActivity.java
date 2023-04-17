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

public class SportsmanProfileActivity extends AppCompatActivity {
    TextView textViewSportsmanNameSurname, textViewSportsmanBirthDate;
    Button buttonSportsmanProfileExit;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference dbUser;

    String uid;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_profile);

        textViewSportsmanNameSurname = findViewById(R.id.textViewSportsmanNameSurname);
        textViewSportsmanBirthDate = findViewById(R.id.textViewSportsmanBirthDate);
        buttonSportsmanProfileExit = findViewById(R.id.buttonSportsmanProfileExit);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                case R.id.notes:
                    // TODO: NotesActivity.class
                    //startActivity(new Intent(getApplicationContext(), CalendarViewActivity.class));
                    //overridePendingTransition(0, 0);
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
                user = dataSnapshot.getValue(User.class);
                String sportsmanNameSurname = Objects.requireNonNull(user).getName() + " " + Objects.requireNonNull(user).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(user).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                textViewSportsmanBirthDate.setText(sportsmanBirthDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        buttonSportsmanProfileExit.setOnClickListener(unused1 -> {
            auth.signOut();
            Intent AuthorizationActivity = new Intent(this, AuthorizationActivity.class);
            startActivity(AuthorizationActivity);
            finish();
        });
    }
}
