package ru.pad;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import ru.pad.objects.User;

public class SportsmanProfileActivity extends AppCompatActivity {
    TextView textViewSportsmanNameSurname;
    TextView textViewSportsmanBirthDate;

    String uid;

    FirebaseDatabase database;
    DatabaseReference user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_profile);

        textViewSportsmanNameSurname = findViewById(R.id.textViewSportsmanNameSurname);
        textViewSportsmanBirthDate = findViewById(R.id.textViewSportsmanBirthDate);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }

        database = FirebaseDatabase.getInstance();
        user = database.getReference("Users/" + uid);
        user.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String sportsmanNameSurname = Objects.requireNonNull(user).getName() + " " + user.getSurname();
                String sportsmanBirthDate = user.getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                textViewSportsmanBirthDate.setText(sportsmanBirthDate);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
