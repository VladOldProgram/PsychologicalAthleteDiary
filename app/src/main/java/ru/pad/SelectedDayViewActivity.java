package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

import ru.pad.objects.Note;

/**
 * Класс объекта активности выбранного дня календаря
 */
public class SelectedDayViewActivity extends AppCompatActivity {
    private String sportsmanUid, role, selectedDayDate;

    private DatabaseReference dbUser;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_day_view);
        getUserData();
        initWidgets();
    }

    /**
     * Сохраняет в текущем контексте данные, переданные из предыдущей активности, сохраняет и обновляет данные пользователя
     */
    private void getUserData() {
        /*
          Получение данных, переданных из предыдущей активности
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
            role = extras.getString("role");
            selectedDayDate = extras.getString("selectedDayDate");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUser = database.getReference("Users/" + sportsmanUid);
    }

    /**
     * Инициализирует виджеты активности
     */
    private void initWidgets() {
        TextView textViewSelectedDayDate = findViewById(R.id.textViewSelectedDayDate);
        textViewSelectedDayDate.setText(selectedDayDate.replace("-", "."));

        TextView textViewNoActions = findViewById(R.id.textViewNoActions);
        textViewNoActions.setVisibility(View.VISIBLE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        /*
          Если пользователь - психолог, и текущая дата не позже выбранного дня,
          то отобразить кнопку назначения тестов
         */
        if (role.equals("Психолог")
                && !LocalDate.now().isAfter(LocalDate.parse(selectedDayDate, formatter))
        ) {
            Button buttonAssignTest = findViewById(R.id.buttonAssignTest);
            buttonAssignTest.setVisibility(View.VISIBLE);
            /*
              Привязка к кнопке функции перехода психолога в активность назначения тестов
             */
            buttonAssignTest.setOnClickListener(unused -> {
                Intent testAssigningActivity = new Intent(SelectedDayViewActivity.this, TestAssigningActivity.class);
                testAssigningActivity.putExtra("sportsmanUid", sportsmanUid);
                testAssigningActivity.putExtra("selectedDayDate", selectedDayDate);
                startActivity(testAssigningActivity);
                overridePendingTransition(0, 0);
            });
        }

        LinearLayout linearLayoutAvailableTests = findViewById(R.id.linearLayoutAvailableTests);
        LinearLayout linearLayoutCompletedTests = findViewById(R.id.linearLayoutCompletedTests);
        LinearLayout linearLayoutNotCompletedTests = findViewById(R.id.linearLayoutNotCompletedTests);
        LinearLayout linearLayoutAssignedTests = findViewById(R.id.linearLayoutAssignedTests);
        LinearLayout linearLayoutNotes = findViewById(R.id.linearLayoutNotes);

        RecyclerView recyclerViewAvailableTests = findViewById(R.id.recyclerViewAvailableTests);
        recyclerViewAvailableTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAvailableTests.setHasFixedSize(true);
        RecyclerView recyclerViewCompletedTests = findViewById(R.id.recyclerViewCompletedTests);
        recyclerViewCompletedTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompletedTests.setHasFixedSize(true);
        RecyclerView recyclerViewNotCompletedTests = findViewById(R.id.recyclerViewNotCompletedTests);
        recyclerViewNotCompletedTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotCompletedTests.setHasFixedSize(true);
        RecyclerView recyclerViewAssignedTests = findViewById(R.id.recyclerViewAssignedTests);
        recyclerViewAssignedTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAssignedTests.setHasFixedSize(true);
        RecyclerView recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotes.setHasFixedSize(true);

        /*
          Запрос к БД на получение доступных для прохождения, непройденных, назначенных тестов
         */
        dbUser
                .child("activity")
                .child(selectedDayDate)
                .child("availableTests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, String> availableTests = (Map<String, String>) snapshot.getValue();

                        /*
                          Если доступных для прохождения тестов нет,
                          то спрятать разделы доступных для прохождения, непройденных, назначенных тестов и вернуться
                         */
                        if (availableTests == null) {
                            linearLayoutAvailableTests.setVisibility(View.GONE);
                            recyclerViewAvailableTests.setVisibility(View.GONE);

                            linearLayoutNotCompletedTests.setVisibility(View.GONE);
                            recyclerViewNotCompletedTests.setVisibility(View.GONE);

                            linearLayoutAssignedTests.setVisibility(View.GONE);
                            recyclerViewAssignedTests.setVisibility(View.GONE);

                            return;
                        }

                        /*
                          Если текущая дата совпадает с выбранным днем календаря
                         */
                        if (LocalDate.now().isEqual(LocalDate.parse(selectedDayDate, formatter))) {
                            /*
                              То отобразить раздел доступных для прохождения тестов,
                              спрятать разделы непройденных и назначенных тестов,
                              спрятать сообщение об отсутствии активностей в этот день
                             */
                            linearLayoutAvailableTests.setVisibility(View.VISIBLE);
                            recyclerViewAvailableTests.setVisibility(View.VISIBLE);

                            linearLayoutNotCompletedTests.setVisibility(View.GONE);
                            recyclerViewNotCompletedTests.setVisibility(View.GONE);

                            linearLayoutAssignedTests.setVisibility(View.GONE);
                            recyclerViewAssignedTests.setVisibility(View.GONE);

                            textViewNoActions.setVisibility(View.GONE);

                            ArrayList<String> availableTestNames = new ArrayList<>();
                            ArrayList<String> availableTestUrls = new ArrayList<>();
                            availableTests.forEach((key, value) -> {
                                availableTestNames.add(key);
                                availableTestUrls.add(value);
                            });

                            RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                    availableTestNames,
                                    availableTestUrls,
                                    null,
                                    "availableTests",
                                    sportsmanUid,
                                    selectedDayDate,
                                    role,
                                    SelectedDayViewActivity.this
                            );
                            recyclerViewAvailableTests.setAdapter(recyclerViewActivitiesAdapter);
                        }
                        /*
                          Иначе если текущая дата позже выбранного дня календаря
                         */
                        else if (LocalDate.now().isAfter(LocalDate.parse(selectedDayDate, formatter))) {
                            /*
                              То отобразить раздел непройденных тестов,
                              спрятать разделы доступных для прохождения и назначенных тестов,
                              спрятать сообщение об отсутствии активностей в этот день
                             */
                            linearLayoutNotCompletedTests.setVisibility(View.VISIBLE);
                            recyclerViewNotCompletedTests.setVisibility(View.VISIBLE);

                            linearLayoutAvailableTests.setVisibility(View.GONE);
                            recyclerViewAvailableTests.setVisibility(View.GONE);

                            linearLayoutAssignedTests.setVisibility(View.GONE);
                            recyclerViewAssignedTests.setVisibility(View.GONE);

                            textViewNoActions.setVisibility(View.GONE);

                            ArrayList<String> notCompletedTestNames = new ArrayList<>();
                            ArrayList<String> notCompletedTestUrls = new ArrayList<>();
                            availableTests.forEach((key, value) -> {
                                notCompletedTestNames.add(key);
                                notCompletedTestUrls.add(value);
                            });

                            RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                    notCompletedTestNames,
                                    notCompletedTestUrls,
                                    null,
                                    "notCompletedTests",
                                    sportsmanUid,
                                    selectedDayDate,
                                    role,
                                    SelectedDayViewActivity.this
                            );
                            recyclerViewNotCompletedTests.setAdapter(recyclerViewActivitiesAdapter);
                        }
                        /*
                          Иначе если текущая дата раньше выбранного дня календаря
                         */
                        else if (LocalDate.now().isBefore(LocalDate.parse(selectedDayDate, formatter))) {
                            /*
                              То отобразить раздел назначенных тестов,
                              спрятать разделы доступных для прохождения и непройденных тестов,
                              спрятать сообщение об отсутствии активностей в этот день
                             */
                            linearLayoutAssignedTests.setVisibility(View.VISIBLE);
                            recyclerViewAssignedTests.setVisibility(View.VISIBLE);

                            linearLayoutAvailableTests.setVisibility(View.GONE);
                            recyclerViewAvailableTests.setVisibility(View.GONE);

                            linearLayoutNotCompletedTests.setVisibility(View.GONE);
                            recyclerViewNotCompletedTests.setVisibility(View.GONE);

                            textViewNoActions.setVisibility(View.GONE);

                            ArrayList<String> assignedTestNames = new ArrayList<>();
                            ArrayList<String> assignedTestUrls = new ArrayList<>();
                            availableTests.forEach((key, value) -> {
                                assignedTestNames.add(key);
                                assignedTestUrls.add(value);
                            });

                            RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                    assignedTestNames,
                                    assignedTestUrls,
                                    null,
                                    "assignedTests",
                                    sportsmanUid,
                                    selectedDayDate,
                                    role,
                                    SelectedDayViewActivity.this
                            );
                            recyclerViewAssignedTests.setAdapter(recyclerViewActivitiesAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(
                                SelectedDayViewActivity.this,
                                "Произошла ошибка при получении доступных для прохождения тестов (" + error.getMessage() + ")",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

        /*
          Запрос к БД на получение пройденных тестов
         */
        dbUser
                .child("activity")
                .child(selectedDayDate)
                .child("completedTests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Map<String, String> completedTests = (Map<String, String>) snapshot.getValue();

                        /*
                          Если пройденных тестов нет,
                          то спрятать раздел пройденных тестов и вернуться
                         */
                        if (completedTests == null) {
                            linearLayoutCompletedTests.setVisibility(View.GONE);
                            recyclerViewCompletedTests.setVisibility(View.GONE);

                            return;
                        }

                        /*
                          Отобразить раздел пройденных тестов,
                          спрятать сообщение об отсутствии активностей в этот день
                         */
                        linearLayoutCompletedTests.setVisibility(View.VISIBLE);
                        recyclerViewCompletedTests.setVisibility(View.VISIBLE);

                        textViewNoActions.setVisibility(View.GONE);

                        ArrayList<String> completedTestNames = new ArrayList<>();
                        ArrayList<String> completedTestUrls = new ArrayList<>();
                        completedTests.forEach((key, value) -> {
                            completedTestNames.add(key);
                            completedTestUrls.add(value);
                        });

                        RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                completedTestNames,
                                completedTestUrls,
                                null,
                                "completedTests",
                                sportsmanUid,
                                selectedDayDate,
                                role,
                                SelectedDayViewActivity.this
                        );
                        recyclerViewCompletedTests.setAdapter(recyclerViewActivitiesAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(
                                SelectedDayViewActivity.this,
                                "Произошла ошибка  при получении пройденных тестов (" + error.getMessage() + ")",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });

        /*
          Запрос к БД на получение заметок
         */
        dbUser
                .child("activity")
                .child(selectedDayDate)
                .child("notes")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        /*
                          Если заметок нет,
                          то спрятать раздел заметок и вернуться
                         */
                        if (!snapshot.hasChildren()) {
                            linearLayoutNotes.setVisibility(View.GONE);
                            recyclerViewNotes.setVisibility(View.GONE);

                            return;
                        }

                        /*
                          Отобразить раздел заметок,
                          спрятать сообщение об отсутствии активностей в этот день
                         */
                        linearLayoutNotes.setVisibility(View.VISIBLE);
                        recyclerViewNotes.setVisibility(View.VISIBLE);

                        textViewNoActions.setVisibility(View.GONE);

                        ArrayList<Note> notes = new ArrayList<>();
                        ArrayList<String> noteNames = new ArrayList<>();
                        for (DataSnapshot children : snapshot.getChildren()) {
                            Note note = children.getValue(Note.class);
                            notes.add(note);
                            noteNames.add(children.getKey());
                        }

                        RecyclerViewActivitiesAdapter recyclerViewActivitiesAdapter = new RecyclerViewActivitiesAdapter(
                                noteNames,
                                null,
                                notes,
                                "notes",
                                sportsmanUid,
                                selectedDayDate,
                                role,
                                SelectedDayViewActivity.this
                        );
                        recyclerViewNotes.setAdapter(recyclerViewActivitiesAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(
                                SelectedDayViewActivity.this,
                                "Произошла ошибка при получении заметок (" + error.getMessage() + ")",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
