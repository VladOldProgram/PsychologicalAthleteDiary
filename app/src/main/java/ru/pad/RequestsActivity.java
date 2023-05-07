package ru.pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.pad.objects.Psychologist;
import ru.pad.objects.Sportsman;

public class RequestsActivity extends AppCompatActivity {
    private String psychologistUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        getUserData();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            psychologistUid = extras.getString("psychologistUid");
        }

        TextView textViewNoRequests = findViewById(R.id.textViewNoRequests);

        RecyclerView recyclerViewRequests = findViewById(R.id.recyclerViewRequests);
        recyclerViewRequests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewRequests.setHasFixedSize(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUsers = database.getReference("Users");

        DatabaseReference dbUser = dbUsers.child(psychologistUid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Psychologist psychologist = dataSnapshot.getValue(Psychologist.class);

                if (psychologist == null
                        || psychologist.getRequests() == null
                        || psychologist.getRequests().size() == 0
                ) {
                    textViewNoRequests.setVisibility(View.VISIBLE);
                    recyclerViewRequests.setVisibility(View.GONE);
                }
                if (psychologist != null
                        && psychologist.getRequests() != null
                        && psychologist.getRequests().size() > 0
                ) {
                    Map<String, String> requests = new HashMap<>(psychologist.getRequests());
                    ArrayList<String> items = new ArrayList<>();
                    requests.forEach((key, value) -> {
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
                                RecyclerViewRequestsAdapter recyclerViewRequestsAdapter = new RecyclerViewRequestsAdapter(items, psychologistUid);
                                recyclerViewRequests.setAdapter(recyclerViewRequestsAdapter);

                                textViewNoRequests.setVisibility(View.GONE);
                                recyclerViewRequests.setVisibility(View.VISIBLE);
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
}