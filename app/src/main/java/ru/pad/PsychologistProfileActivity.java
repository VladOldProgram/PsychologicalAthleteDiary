package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import java.util.Map;
import java.util.Objects;

import ru.pad.objects.Psychologist;
import ru.pad.objects.Sportsman;

/**
 * Класс объекта активности профиля психолога
 */
public class PsychologistProfileActivity extends AppCompatActivity {
    private Button buttonRequests;

    private String psychologistUid;

    private FirebaseAuth auth;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_psychologist_profile);
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
            psychologistUid = extras.getString("psychologistUid");
        }

        TextView textViewPsychologistNameSurname = findViewById(R.id.textViewPsychologistNameSurname);
        TextView textViewPsychologistBirthDate = findViewById(R.id.textViewPsychologistBirthDate);

        buttonRequests = findViewById(R.id.buttonRequests);

        TextView textViewNoSportsmen = findViewById(R.id.textViewNoSportsmen);

        RecyclerView recyclerViewSportsmen = findViewById(R.id.recyclerViewSportsmen);
        recyclerViewSportsmen.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSportsmen.setHasFixedSize(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUsers = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        /*
          Запрос к БД на получение данных психолога
         */
        dbUsers.child(psychologistUid).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Psychologist psychologist = dataSnapshot.getValue(Psychologist.class);

                /*
                  Заполнение полей интерфейса профиля информацией о психологе
                 */
                String psychologistNameSurname = Objects.requireNonNull(psychologist).getName() + " " + psychologist.getSurname();
                String psychologistBirthDate = Objects.requireNonNull(psychologist).getBirthDate();
                textViewPsychologistNameSurname.setText(psychologistNameSurname);
                textViewPsychologistBirthDate.setText(psychologistBirthDate);

                /*
                  Отображение количества заявок спортсменов
                 */
                if (psychologist.getRequests() != null) {
                    buttonRequests.setText("Заявки (" + psychologist.getRequests().size() + ")");
                } else buttonRequests.setText("Заявки (0)");

                /*
                  Если список спортсменов психолога пуст,
                  то отобразить сообщение об отсутствии спортсменов и спрятать список спортсменов
                 */
                if (psychologist.getSportsmen() == null || psychologist.getSportsmen().size() == 0) {
                    textViewNoSportsmen.setVisibility(View.VISIBLE);
                    recyclerViewSportsmen.setVisibility(View.GONE);
                }
                /*
                  Если список спортсменов психолога непуст
                 */
                else if (psychologist.getSportsmen() != null && psychologist.getSportsmen().size() > 0) {
                    Map<String, String> sportsmen = new HashMap<>(psychologist.getSportsmen());
                    ArrayList<String> items = new ArrayList<>();
                    /*
                      То для каждого спортсмена запрос к БД на получение его данных
                      и обновление списка спортсменов в интерфейсе
                     */
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
                                RecyclerViewSportsmenAdapter recyclerViewSportsmenAdapter = new RecyclerViewSportsmenAdapter(items, psychologistUid, PsychologistProfileActivity.this);
                                recyclerViewSportsmen.setAdapter(recyclerViewSportsmenAdapter);

                                /*
                                  И спрятать сообщение об отсутствии спортсменов и отобразить список спортсменов
                                 */
                                textViewNoSportsmen.setVisibility(View.GONE);
                                recyclerViewSportsmen.setVisibility(View.VISIBLE);
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

    /**
     * Инициализирует виджеты активности
     */
    private void initWidgets() {
        Button buttonPsychologistProfileExit = findViewById(R.id.buttonPsychologistProfileExit);
        /*
          Привязка к кнопке функции выхода из текущего аккаунта и перехода в активность авторизации
         */
        buttonPsychologistProfileExit.setOnClickListener(unused1 -> {
            auth.signOut();
            Intent AuthorizationActivity = new Intent(this, AuthorizationActivity.class);
            startActivity(AuthorizationActivity);
            finish();
        });

        /*
          Привязка к кнопке функции открытия активности заявок психолога
         */
        buttonRequests.setOnClickListener(unused2 -> {
            Intent requestsActivity = new Intent(this, RequestsActivity.class);
            requestsActivity.putExtra("psychologistUid", psychologistUid);
            startActivity(requestsActivity);
        });
    }
}
