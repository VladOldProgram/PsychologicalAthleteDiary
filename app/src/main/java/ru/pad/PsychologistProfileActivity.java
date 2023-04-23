package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ru.pad.objects.Psychologist;
import ru.pad.objects.Sportsman;

public class PsychologistProfileActivity extends AppCompatActivity {
    private String uid;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }

        TextView textViewPsychologistNameSurname = findViewById(R.id.textViewPsychologistNameSurname);
        TextView textViewPsychologistBirthDate = findViewById(R.id.textViewPsychologistBirthDate);

        RecyclerView recyclerViewSportsmen = findViewById(R.id.recyclerViewSportsmen);
        recyclerViewSportsmen.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSportsmen.setHasFixedSize(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUsers = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        DatabaseReference dbUser = dbUsers.child(uid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Psychologist psychologist = dataSnapshot.getValue(Psychologist.class);
                String psychologistNameSurname = Objects.requireNonNull(psychologist).getName() + " " + psychologist.getSurname();
                String psychologistBirthDate = Objects.requireNonNull(psychologist).getBirthDate();
                textViewPsychologistNameSurname.setText(psychologistNameSurname);
                textViewPsychologistBirthDate.setText(psychologistBirthDate);

                if (psychologist.getSportsmen() != null) {
                    Map<String, String> sportsmen = new HashMap<>(psychologist.getSportsmen());
                    List<String> items = new ArrayList<>();
                    sportsmen.forEach((key, value) -> {
                        dbUsers.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Sportsman sportsman = snapshot.getValue(Sportsman.class);
                                if (sportsman != null
                                        && sportsman.getName() != null
                                        && sportsman.getSurname() != null
                                ) {
                                    items.add(sportsman.getName() + " " + sportsman.getSurname() + " (" + key.replace("•", ".") + ")");
                                }
                                RecyclerViewSportsmenAdapter recyclerViewSportsmenAdapter = new RecyclerViewSportsmenAdapter(items, uid, PsychologistProfileActivity.this);
                                recyclerViewSportsmen.setAdapter(recyclerViewSportsmenAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    });
                }
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
            Intent requestsActivity = new Intent(this, RequestsActivity.class);
            requestsActivity.putExtra("uid", uid);
            startActivity(requestsActivity);
        });
    }
}
