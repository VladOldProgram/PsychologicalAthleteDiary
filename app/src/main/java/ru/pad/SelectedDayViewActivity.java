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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import ru.pad.objects.Note;

public class SelectedDayViewActivity extends AppCompatActivity {
    private LinearLayout linearLayoutAvailableTests, linearLayoutCompletedTests,
            linearLayoutNotCompletedTests, linearLayoutAssignedTests, linearLayoutNotes;
    private RecyclerView recyclerViewAvailableTests, recyclerViewCompletedTests,
            recyclerViewNotCompletedTests, recyclerViewAssignedTests, recyclerViewNotes;

    private String sportsmanUid, role, selectedDayDate;

    private DatabaseReference dbUser;

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
            role = extras.getString("role");
            selectedDayDate = extras.getString("selectedDayDate");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    private void initWidgets() {
        TextView textViewSelectedDayDate = findViewById(R.id.textViewSelectedDayDate);
        textViewSelectedDayDate.setText(selectedDayDate.replace("-", "."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        if (role.equals("Психолог")
                && !LocalDate.now().isAfter(LocalDate.parse(selectedDayDate, formatter))
        ) {
            Button buttonAssignTest = findViewById(R.id.buttonAssignTest);
            buttonAssignTest.setVisibility(View.VISIBLE);
            buttonAssignTest.setOnClickListener(unused -> {
                Intent testAssigningActivity = new Intent(SelectedDayViewActivity.this, TestAssigningActivity.class);
                testAssigningActivity.putExtra("sportsmanUid", sportsmanUid);
                testAssigningActivity.putExtra("selectedDayDate", selectedDayDate);
                startActivity(testAssigningActivity);
                overridePendingTransition(0, 0);
            });
        }

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

        dbUser.child("activity").child(selectedDayDate).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> activities = (Map<String, String>) snapshot.getValue();

                if (activities != null && activities.containsKey("availableTests")) {
                    if (LocalDate.now().isEqual(LocalDate.parse(selectedDayDate, formatter))) {
                        ArrayList<String> availableTestNames = new ArrayList<>();
                        ArrayList<String> availableTestUrls = new ArrayList<>();
                        RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                availableTestNames,
                                availableTestUrls,
                                null,
                                "availableTests",
                                sportsmanUid,
                                role,
                                SelectedDayViewActivity.this
                        );
                        recyclerViewAvailableTests.setAdapter(recyclerViewActivitiesAdapter);
                        linearLayoutAvailableTests.setVisibility(View.VISIBLE);
                        recyclerViewAvailableTests.setVisibility(View.VISIBLE);

                        linearLayoutNotCompletedTests.setVisibility(View.GONE);
                        recyclerViewNotCompletedTests.setVisibility(View.GONE);

                        linearLayoutAssignedTests.setVisibility(View.GONE);
                        recyclerViewAssignedTests.setVisibility(View.GONE);
                    }
                    else if (LocalDate.now().isAfter(LocalDate.parse(selectedDayDate, formatter))) {
                        ArrayList<String> notCompletedTestNames = new ArrayList<>();
                        ArrayList<String> notCompletedTestUrls = new ArrayList<>();
                        RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                notCompletedTestNames,
                                notCompletedTestUrls,
                                null,
                                "notCompletedTests",
                                sportsmanUid,
                                role,
                                SelectedDayViewActivity.this
                        );
                        recyclerViewAvailableTests.setAdapter(recyclerViewActivitiesAdapter);
                        linearLayoutNotCompletedTests.setVisibility(View.VISIBLE);
                        recyclerViewNotCompletedTests.setVisibility(View.VISIBLE);

                        linearLayoutAvailableTests.setVisibility(View.GONE);
                        recyclerViewAvailableTests.setVisibility(View.GONE);

                        linearLayoutAssignedTests.setVisibility(View.GONE);
                        recyclerViewAssignedTests.setVisibility(View.GONE);
                    }
                    else if (LocalDate.now().isBefore(LocalDate.parse(selectedDayDate, formatter))) {
                        ArrayList<String> assignedTestNames = new ArrayList<>();
                        ArrayList<String> assignedTestUrls = new ArrayList<>();
                        RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                assignedTestNames,
                                assignedTestUrls,
                                null,
                                "assignedTests",
                                sportsmanUid,
                                role,
                                SelectedDayViewActivity.this
                        );
                        recyclerViewAvailableTests.setAdapter(recyclerViewActivitiesAdapter);
                        linearLayoutAssignedTests.setVisibility(View.VISIBLE);
                        recyclerViewAssignedTests.setVisibility(View.VISIBLE);

                        linearLayoutAvailableTests.setVisibility(View.GONE);
                        recyclerViewAvailableTests.setVisibility(View.GONE);

                        linearLayoutNotCompletedTests.setVisibility(View.GONE);
                        recyclerViewNotCompletedTests.setVisibility(View.GONE);
                    }
                }
                if (activities != null && activities.containsKey("completedTests")) {
                    ArrayList<String> completedTestNames = new ArrayList<>();
                    ArrayList<String> completedTestUrls = new ArrayList<>();
                    RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                            completedTestNames,
                            completedTestUrls,
                            null,
                            "completedTests",
                            sportsmanUid,
                            role,
                            SelectedDayViewActivity.this
                    );
                    recyclerViewAvailableTests.setAdapter(recyclerViewActivitiesAdapter);
                    linearLayoutCompletedTests.setVisibility(View.VISIBLE);
                    recyclerViewCompletedTests.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutCompletedTests.setVisibility(View.GONE);
                    recyclerViewCompletedTests.setVisibility(View.GONE);
                }
                if (activities != null && activities.containsKey("notes")) {
                    ArrayList<String> noteNames = new ArrayList<>();
                    ArrayList<Note> notes = new ArrayList<>();
                    RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                            noteNames,
                            null,
                            notes,
                            "notes",
                            sportsmanUid,
                            role,
                            SelectedDayViewActivity.this
                    );
                    recyclerViewAvailableTests.setAdapter(recyclerViewActivitiesAdapter);
                    linearLayoutNotes.setVisibility(View.VISIBLE);
                    recyclerViewNotes.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutNotes.setVisibility(View.GONE);
                    recyclerViewNotes.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}
