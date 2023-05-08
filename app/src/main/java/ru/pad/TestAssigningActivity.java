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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Класс объекта активности назначения тестов психологом
 */
public class TestAssigningActivity extends AppCompatActivity implements RecyclerViewAssigningTestsAdapter.OnItemListener {
    private RecyclerView recyclerViewAssigningTests;

    private RecyclerViewAssigningTestsAdapter recyclerViewAssigningTestsAdapter;

    private String sportsmanUid, selectedDayDate;

    private Map<String, String> tests;
    private ArrayList<String> checkedTestNames;

    private FirebaseDatabase database;
    private DatabaseReference dbUsers;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_assigning);
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
            selectedDayDate = extras.getString("selectedDayDate");
        }

        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");
    }

    /**
     * Инициализирует виджеты активности
     */
    private void initWidgets() {
        tests = new HashMap<>();
        checkedTestNames = new ArrayList<>();

        Button buttonAssignTests = findViewById(R.id.buttonAssignTests);
        recyclerViewAssigningTests = findViewById(R.id.recyclerViewAssigningTests);
        recyclerViewAssigningTests.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAssigningTests.setHasFixedSize(true);

        DatabaseReference dbTests = database.getReference("Tests/");

        /*
          Запрос к БД на получение списка тестов, доступных для назначения
         */
        dbTests.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tests = (Map<String, String>) dataSnapshot.getValue();
                ArrayList<String> items = new ArrayList<>(Objects.requireNonNull(tests).keySet());
                recyclerViewAssigningTestsAdapter = new RecyclerViewAssigningTestsAdapter(items, TestAssigningActivity.this);
                recyclerViewAssigningTests.setAdapter(recyclerViewAssigningTestsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        TestAssigningActivity.this,
                        "Произошла ошибка при получении списка тестов (" + error.getMessage() + ")",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        /*
          Привязка к кнопке функции назначения выбранных тестов
         */
        buttonAssignTests.setOnClickListener(unused1 -> {
            /*
              Для каждого выбранного теста
             */
            checkedTestNames.forEach(key -> {
                if (checkedTestNames.isEmpty()) {
                    Toast.makeText(
                            TestAssigningActivity.this,
                            "Не выбрано ни одного теста",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                String currentTime = LocalTime.now().toString();
                int i = currentTime.lastIndexOf(".");
                currentTime = currentTime.substring(0, i);

                /*
                  Запрос к БД на добавление теста спортсмену в раздел доступных для прохождения
                 */
                dbUsers
                        .child(sportsmanUid)
                        .child("activity")
                        .child(selectedDayDate.replace(".", "-"))
                        .child("availableTests")
                        .child(key + " " + currentTime)
                        .setValue(tests.get(key))
                        .addOnSuccessListener(unused2 -> {
                            Toast.makeText(
                                    TestAssigningActivity.this,
                                    "Тесты назначены",
                                    Toast.LENGTH_SHORT
                            ).show();

                            /*
                              После успешного назначения выбор сбрасывается
                             */
                            checkedTestNames.clear();
                            ArrayList<String> items = new ArrayList<>(Objects.requireNonNull(tests).keySet());
                            recyclerViewAssigningTestsAdapter = new RecyclerViewAssigningTestsAdapter(items, TestAssigningActivity.this);
                            recyclerViewAssigningTests.setAdapter(recyclerViewAssigningTestsAdapter);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    TestAssigningActivity.this,
                                    "Произошла ошибка при назначении теста " + key + " (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                        });
            });
        });
    }

    /**
     * Реализует множественный выбор элементов в списке тестов для назначения
     * @param holder холдер элемента списка тестов
     */
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
