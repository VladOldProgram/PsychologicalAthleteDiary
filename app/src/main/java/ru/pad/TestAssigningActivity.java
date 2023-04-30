package ru.pad;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestAssigningActivity extends AppCompatActivity implements RecyclerViewAssigningTestsAdapter.OnItemListener {
    private RecyclerView recyclerViewAssigningTests;

    private String sportsmanUid, selectedDayDate;

    private Map<String, String> tests;
    private ArrayList<String> checkedTestNames;

    private RecyclerViewAssigningTestsAdapter recyclerViewAssigningTestsAdapter;

    private FirebaseDatabase database;
    private DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_assigning);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            selectedDayDate = extras.getString("selectedDayDate");
        }

        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");
    }

    private void initWidgets() {
        tests = new HashMap<>();
        checkedTestNames = new ArrayList<>();

        Button buttonAssignTests = findViewById(R.id.buttonAssignTests);
        recyclerViewAssigningTests = findViewById(R.id.recyclerViewAssigningTests);
        recyclerViewAssigningTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAssigningTests.setHasFixedSize(true);

        DatabaseReference dbTests = database.getReference("Tests/");
        dbTests.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests = (Map<String, String>) dataSnapshot.getValue();
                ArrayList<String> items = new ArrayList<>(Objects.requireNonNull(tests).keySet());
                recyclerViewAssigningTestsAdapter = new RecyclerViewAssigningTestsAdapter(items, TestAssigningActivity.this);
                recyclerViewAssigningTests.setAdapter(recyclerViewAssigningTestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        buttonAssignTests.setOnClickListener(unused1 -> {
            checkedTestNames.forEach(key -> {
                dbUsers
                        .child(sportsmanUid)
                        .child("activity")
                        .child(selectedDayDate.replace(".", "-"))
                        .child("availableTests")
                        .child(key)
                        .setValue(tests.get(key));
            });

            if (checkedTestNames.isEmpty()) {
                Toast.makeText(
                        TestAssigningActivity.this,
                        "Не выбрано ни одного теста",
                        Toast.LENGTH_SHORT
                ).show();
            } else {
                Toast.makeText(
                        TestAssigningActivity.this,
                        "Тесты назначены",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public void onItemClick(RecyclerViewAssigningTestsHolder holder) {
        int position = holder.getAbsoluteAdapterPosition();
        if (recyclerViewAssigningTestsAdapter.isSelected(position)) {
            checkedTestNames.remove(holder.textViewTestName.getText().toString());
        } else {
            checkedTestNames.add(holder.textViewTestName.getText().toString());
        }
        recyclerViewAssigningTestsAdapter.toggleSelection(position);
    }
}
