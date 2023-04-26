package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ru.pad.objects.Psychologist;
import ru.pad.objects.Sportsman;

public class SelectedDayViewActivity extends AppCompatActivity {
    private TextView textViewSelectedDayDate;
    private LinearLayout linearLayoutAvailableTests, linearLayoutCompletedTests,
            linearLayoutNotCompletedTests, linearLayoutAssignedTests, linearLayoutNotes;
    private RecyclerView recyclerViewAvailableTests, recyclerViewCompletedTests,
            recyclerViewNotCompletedTests, recyclerViewAssignedTests, recyclerViewNotes;
    private Button buttonAssignTest;

    private String sportsmanUid, psychologistUid, role, selectedDayDate;

    private Sportsman sportsman;
    private Psychologist psychologist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day_view);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            psychologistUid = extras.getString("psychologistUid");
            role = extras.getString("role");
            selectedDayDate = extras.getString("selectedDayDate");
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

        if (role.equals("Психолог")) {
            dbUser = database.getReference("Users/" + psychologistUid);
            dbUser.addValueEventListener(new ValueEventListener() {
                @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    psychologist = dataSnapshot.getValue(Psychologist.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        }
    }

    private void initWidgets() {
        textViewSelectedDayDate = findViewById(R.id.textViewSelectedDayDate);
        textViewSelectedDayDate.setText(selectedDayDate);

        linearLayoutAvailableTests = findViewById(R.id.linearLayoutAvailableTests);
        linearLayoutAvailableTests = findViewById(R.id.linearLayoutAvailableTests);
        linearLayoutCompletedTests = findViewById(R.id.linearLayoutCompletedTests);
        linearLayoutNotCompletedTests = findViewById(R.id.linearLayoutNotCompletedTests);
        linearLayoutAssignedTests = findViewById(R.id.linearLayoutAssignedTests);
        linearLayoutNotes = findViewById(R.id.linearLayoutNotes);

        recyclerViewAvailableTests = findViewById(R.id.recyclerViewAvailableTests);
        recyclerViewAvailableTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAvailableTests.setHasFixedSize(true);
        recyclerViewCompletedTests = findViewById(R.id.recyclerViewCompletedTests);
        recyclerViewCompletedTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompletedTests.setHasFixedSize(true);
        recyclerViewNotCompletedTests = findViewById(R.id.recyclerViewNotCompletedTests);
        recyclerViewNotCompletedTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotCompletedTests.setHasFixedSize(true);
        recyclerViewAssignedTests = findViewById(R.id.recyclerViewAssignedTests);
        recyclerViewAssignedTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAssignedTests.setHasFixedSize(true);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setHasFixedSize(true);

        if (role.equals("Психолог")) {
            buttonAssignTest = findViewById(R.id.buttonAssignTest);
            buttonAssignTest.setVisibility(View.VISIBLE);
            buttonAssignTest.setOnClickListener(unused -> {
                Intent testAssigningActivity = new Intent(SelectedDayViewActivity.this, TestAssigningActivity.class);
                testAssigningActivity.putExtra("sportsmanUid", sportsmanUid);
                testAssigningActivity.putExtra("selectedDayDate", selectedDayDate);
                startActivity(testAssigningActivity);
                overridePendingTransition(0, 0);
            });
        }
    }
}
