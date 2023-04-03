package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ru.pad.models.User;

public class PsychologistProfileActivity extends AppCompatActivity {
    Button buttonRequests;
    TextView textViewPsychologistNameSurname;
    TextView textViewPsychologistBirthDate;

    String uid;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);

        textViewPsychologistNameSurname = findViewById(R.id.textViewPsychologistNameSurname);
        textViewPsychologistBirthDate = findViewById(R.id.textViewPsychologistBirthDate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users/" + uid);
        users.addValueEventListener(new ValueEventListener() {
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

        buttonRequests = findViewById(R.id.buttonRequests);
        buttonRequests.setOnClickListener(view -> {
            Intent psychologistRequestsActivity = new Intent(this, PsychologistRequestsActivity.class);
            startActivity(psychologistRequestsActivity);
        });
    }
}
