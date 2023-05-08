package ru.pad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.pad.objects.Sportsman;

/**
 * Класс объекта активности профиля выбранного психологом спортсмена
 */
public class SelectedSportsmanProfileActivity  extends AppCompatActivity {
    private TextView textViewSportsmanBirthDate, textViewSportsmanNameSurname;

    private Sportsman sportsman;

    private DatabaseReference dbUsers;

    private String psychologistUid, sportsmanUid;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_sportsman_profile);
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
            sportsmanUid = extras.getString("sportsmanUid");
        }

        textViewSportsmanNameSurname = findViewById(R.id.textViewSportsmanNameSurname);
        textViewSportsmanBirthDate = findViewById(R.id.textViewSportsmanBirthDate);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");

        DatabaseReference dbUser = dbUsers.child(sportsmanUid);
        /*
          Запрос к БД на получение данных спортсмена
         */
        dbUser.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sportsman = dataSnapshot.getValue(Sportsman.class);

                /*
                  Заполнение полей интерфейса профиля информацией о спортсмене
                 */
                String sportsmanNameSurname = Objects.requireNonNull(sportsman).getName() + " " + Objects.requireNonNull(sportsman).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(sportsman).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                int age = Period.between(LocalDate.parse(sportsmanBirthDate, dtf), LocalDate.now()).getYears();
                textViewSportsmanBirthDate.setText(sportsmanBirthDate + " (" + age + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        SelectedSportsmanProfileActivity.this,
                        "Произошла ошибка при получении данных вашего профиля (" + error.getMessage() + ")",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    /**
     * Инициализирует виджеты активности
     */
    private void initWidgets() {
        Button buttonSportsmanCalendar = findViewById(R.id.buttonSportsmanCalendar);
        /*
          Привязка к кнопке функции открытия психологом активности календаря спортсмена
         */
        buttonSportsmanCalendar.setOnClickListener(unused1 -> {
            Intent calendarViewActivity = new Intent(SelectedSportsmanProfileActivity.this, CalendarViewActivity.class);
            calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
            calendarViewActivity.putExtra("role", "Психолог");
            startActivity(calendarViewActivity);
            overridePendingTransition(0, 0);
        });

        Button buttonDeleteSportsman = findViewById(R.id.buttonDeleteSportsman);
        /*
          Привязка к кнопке функции удаления психологом спортсмена
         */
        buttonDeleteSportsman.setOnClickListener(unused2 -> {
            /*
              Создание, настройка и отображение диалогового окна подтверждения удаления
             */
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_delete_sportsman);
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(wlp);
            Button buttonCancelDelete = dialog.findViewById(R.id.buttonCancelDelete);
            buttonCancelDelete.setOnClickListener(unused3 -> {
                dialog.dismiss();
            });
            Button buttonConfirmDelete = dialog.findViewById(R.id.buttonConfirmDelete);
            buttonConfirmDelete.setOnClickListener(unused4 -> {
                /*
                  Запрос к БД на удаление психолога у спортсмена
                 */
                AtomicBoolean noException = new AtomicBoolean(true);
                dbUsers
                        .child(sportsmanUid)
                        .child("psychologist")
                        .setValue("")
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    SelectedSportsmanProfileActivity.this,
                                    "Произошла ошибка при удалении спортсмена (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                            noException.set(false);
                        });
                /*
                  Запрос к БД на установление значения false статусу заявки спортсмена
                 */
                dbUsers
                        .child(sportsmanUid)
                        .child("requestAccepted")
                        .setValue(false)
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    SelectedSportsmanProfileActivity.this,
                                    "Произошла ошибка при удалении спортсмена (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                            noException.set(false);
                        });
                /*
                  Запрос к БД на удаление спортсмена из списка спортсменов у психолога
                 */
                dbUsers
                        .child(psychologistUid)
                        .child("sportsmen")
                        .child(sportsmanUid)
                        .removeValue()
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    SelectedSportsmanProfileActivity.this,
                                    "Произошла ошибка при удалении спортсмена (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                            noException.set(false);
                        });
                if (noException.get()) {
                    finish();
                }
                dialog.dismiss();
            });
            dialog.setCancelable(false);
            dialog.show();
        });
    }
}
