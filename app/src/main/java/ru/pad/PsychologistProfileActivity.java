package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ru.pad.objects.User;

public class PsychologistProfileActivity extends AppCompatActivity {
    private TextView textViewPsychologistNameSurname, textViewPsychologistBirthDate;

    private String uid;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        textViewPsychologistNameSurname = findViewById(R.id.textViewPsychologistNameSurname);
        textViewPsychologistBirthDate = findViewById(R.id.textViewPsychologistBirthDate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }

        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        DatabaseReference dbUser = dbUsers.child(uid);
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
    }

    private void initWidgets() {
        Button buttonPsychologistProfileExit = findViewById(R.id.buttonPsychologistProfileExit);
        Button buttonRequests = findViewById(R.id.buttonRequests);

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
