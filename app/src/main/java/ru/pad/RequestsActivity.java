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

/**
 * Класс объекта активности обработки заявок психологом
 */
public class RequestsActivity extends AppCompatActivity {
    private String psychologistUid;

    /**
     * Отображает эту активность на экране, вызывает метод <b>getUserData()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getRequestsData()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        getRequestsData();
    }

    /**
     * Сохраняет в текущем контексте данные, переданные из предыдущей активности,
     * сохраняет и обновляет данные заявок спортсменов психологу
     */
    private void getRequestsData() {
        /*
          Получение данных, переданных из предыдущей активности
         */
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

        /*
          Запрос к БД на получение данных психолога
         */
        dbUsers.child(psychologistUid).addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Psychologist psychologist = dataSnapshot.getValue(Psychologist.class);

                /*
                  Если список заявок пуст, то отобразить сообщение об отсутствии заявок и спрятать список заявок
                 */
                if (psychologist == null
                        || psychologist.getRequests() == null
                        || psychologist.getRequests().size() == 0
                ) {
                    textViewNoRequests.setVisibility(View.VISIBLE);
                    recyclerViewRequests.setVisibility(View.GONE);
                }
                /*
                  Если список заявок непуст
                 */
                if (psychologist != null
                        && psychologist.getRequests() != null
                        && psychologist.getRequests().size() > 0
                ) {
                    Map<String, String> requests = new HashMap<>(psychologist.getRequests());
                    ArrayList<String> items = new ArrayList<>();
                    /*
                      То для каждой заявки запрос к БД на получение данных этого спортсмена
                      и обновление списка заявок в интерфейсе
                     */
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

                                /*
                                  И спрятать сообщение об отсутствии заявок и отобразить список заявок
                                 */
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