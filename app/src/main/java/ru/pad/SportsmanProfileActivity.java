package ru.pad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import ru.pad.objects.Psychologist;
import ru.pad.objects.Sportsman;

/**
 * Класс объекта активности профиля спортсмена
 */
public class SportsmanProfileActivity extends AppCompatActivity {
    protected TextView textViewAddPsychologistStatus, textViewCurrentPsychologistNameSurname,
            textViewSportsmanBirthDate, textViewSportsmanNameSurname;
    protected EditText editTextPsychologistEmail;
    protected LinearLayout linearLayoutAddPsychologist, linearLayoutCurrentPsychologistNameSurname;
    protected Button buttonDeletePsychologist, buttonSportsmanProfileExit;
    protected BottomNavigationView bottomNavigationView;
    protected FloatingActionButton floatingActionButtonAddPsychologist;

    protected String sportsmanUid;

    protected FirebaseAuth auth;
    protected DatabaseReference dbUsers;

    protected Psychologist psychologist;
    protected Sportsman sportsman;

    /**
     * Отображает эту активность на экране, вызывает методы <b>getUserData()</b>, <b>initWidgets()</b>
     * @param savedInstanceState сохраненное состояние активности
     * @see #getUserData()
     * @see #initWidgets()
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_profile);
        getUserData();
        initWidgets();
    }

    /**
     * Сохраняет в текущем контексте данные, переданные из предыдущей активности, сохраняет и обновляет данные пользователя
     */
    protected void getUserData() {
        /*
          Получение данных, переданных из предыдущей активности
         */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sportsmanUid = extras.getString("sportsmanUid");
        }

        textViewSportsmanNameSurname = findViewById(R.id.textViewSportsmanNameSurname);
        textViewSportsmanBirthDate = findViewById(R.id.textViewSportsmanBirthDate);

        editTextPsychologistEmail = findViewById(R.id.editTextPsychologistEmail);
        linearLayoutAddPsychologist = findViewById(R.id.linearLayoutAddPsychologist);
        linearLayoutCurrentPsychologistNameSurname = findViewById(R.id.linearLayoutCurrentPsychologistNameSurname);
        textViewCurrentPsychologistNameSurname = findViewById(R.id.textViewCurrentPsychologistNameSurname);
        textViewAddPsychologistStatus = findViewById(R.id.textViewAddPsychologistStatus);
        buttonDeletePsychologist = findViewById(R.id.buttonDeletePsychologist);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        DatabaseReference dbUser = dbUsers.child(sportsmanUid);
        /*
          Запрос к БД на получение данных спортсмена
         */
        dbUser.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sportsman = dataSnapshot.getValue(Sportsman.class);

                /*
                  Заполнение полей интерфейса профиля информацией о спортсмене
                 */
                String sportsmanNameSurname = Objects.requireNonNull(sportsman).getName() + " " + Objects.requireNonNull(sportsman).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(sportsman).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                textViewSportsmanBirthDate.setText(sportsmanBirthDate);

                /*
                  Если у спортсмена нет психолога,
                  то отобразить интерфейс для добавления психолога и спрятать интерфейс текущего психолога
                 */
                if (sportsman.getPsychologist() == null || sportsman.getPsychologist().equals("")) {
                    linearLayoutAddPsychologist.setVisibility(View.VISIBLE);
                    editTextPsychologistEmail.setText("");
                    editTextPsychologistEmail.setVisibility(View.VISIBLE);
                    textViewAddPsychologistStatus.setText("");
                    textViewAddPsychologistStatus.setVisibility(View.VISIBLE);
                    textViewCurrentPsychologistNameSurname.setText("");
                    textViewCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    linearLayoutCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    buttonDeletePsychologist.setVisibility(View.GONE);
                    return;
                }

                /*
                  Если заявка спортсмена отклонена,
                  то вывести соответствующее сообщение,
                  отобразить интерфейс для добавления психолога и спрятать интерфейс текущего психолога
                 */
                if (sportsman.getPsychologist().equals("declined")) {
                    linearLayoutAddPsychologist.setVisibility(View.VISIBLE);
                    editTextPsychologistEmail.setVisibility(View.VISIBLE);
                    textViewAddPsychologistStatus.setText("Ваша заявка отклонена");
                    textViewAddPsychologistStatus.setVisibility(View.VISIBLE);
                    textViewCurrentPsychologistNameSurname.setText("");
                    textViewCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    linearLayoutCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    buttonDeletePsychologist.setVisibility(View.GONE);
                    return;
                }

                /*
                  Если заявка спортсмена еще не принята,
                  то вывести соответствующее сообщение,
                  отобразить интерфейс для добавления психолога и спрятать интерфейс текущего психолога
                 */
                if (!sportsman.getRequestAccepted()) {
                    linearLayoutAddPsychologist.setVisibility(View.VISIBLE);
                    editTextPsychologistEmail.setVisibility(View.VISIBLE);
                    textViewAddPsychologistStatus.setText("Ожидание ответа от " + sportsman.getPsychologist());
                    textViewAddPsychologistStatus.setVisibility(View.VISIBLE);
                    textViewCurrentPsychologistNameSurname.setText("");
                    textViewCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    linearLayoutCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    buttonDeletePsychologist.setVisibility(View.GONE);
                    return;
                }

                /*
                  Иначе - запрос к БД на получение данных психолога спортсмена
                 */
                dbUsers.child(sportsman.getPsychologist().replace(".", "•")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Psychologist psychologist = snapshot.getValue(Psychologist.class);
                        if (psychologist != null
                                && psychologist.getName() != null
                                && psychologist.getSurname() != null
                        ) {
                            /*
                              И спрятать интерфейс для добавления психолога и отобразить интерфейс текущего психолога
                             */
                            linearLayoutAddPsychologist.setVisibility(View.GONE);
                            editTextPsychologistEmail.setVisibility(View.GONE);
                            textViewAddPsychologistStatus.setVisibility(View.GONE);
                            textViewCurrentPsychologistNameSurname.setText(psychologist.getName() + " " + psychologist.getSurname());
                            textViewCurrentPsychologistNameSurname.setVisibility(View.VISIBLE);
                            linearLayoutCurrentPsychologistNameSurname.setVisibility(View.VISIBLE);
                            buttonDeletePsychologist.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(
                                SportsmanProfileActivity.this,
                                "Произошла ошибка при получении данных вашего психолога (" + error.getMessage() + ")",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(
                        SportsmanProfileActivity.this,
                        "Произошла ошибка при получении данных вашего профиля (" + error.getMessage() + ")",
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    /**
     * Инициализирует виджеты активности
     */
    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    protected void initWidgets() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        /*
          Привязка к кнопкам меню функций перехода в выбранную активность
         */
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    Intent calendarViewActivity = new Intent(SportsmanProfileActivity.this, CalendarViewActivity.class);
                    calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
                    calendarViewActivity.putExtra("role", "Спортсмен");
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    finish();
                    return true;
                case R.id.notes:
                    Intent noteWritingActivity = new Intent(SportsmanProfileActivity.this, NoteWritingActivity.class);
                    noteWritingActivity.putExtra("sportsmanUid", sportsmanUid);
                    noteWritingActivity.putExtra("role", "Спортсмен");
                    startActivity(noteWritingActivity);
                    overridePendingTransition(0, 0);
                    finishAffinity();
                    finish();
                    return true;
                case R.id.profile:
                    return true;
            }
            return false;
        });

        buttonSportsmanProfileExit = findViewById(R.id.buttonSportsmanProfileExit);
        /*
          Привязка к кнопке функции выхода из текущего аккаунта и перехода в активность авторизации
         */
        buttonSportsmanProfileExit.setOnClickListener(unused1 -> {
            auth.signOut();
            Intent AuthorizationActivity = new Intent(this, AuthorizationActivity.class);
            startActivity(AuthorizationActivity);
            finishAffinity();
            finish();
        });

        /*
          Привязка к кнопке функции добавления психолога спортсмену
         */
        floatingActionButtonAddPsychologist = findViewById(R.id.floatingActionButtonAddPsychologist);
        floatingActionButtonAddPsychologist.setOnClickListener(unused2 -> {
            psychologist = null;
            if (editTextPsychologistEmail.getText().toString().equals("")) {
                textViewAddPsychologistStatus.setText("Введите email");
                return;
            }

            DatabaseReference dbUser = dbUsers.child(editTextPsychologistEmail.getText().toString().replace(".", "•"));
            /*
              Запрос к БД на получение данных указанного психолога
             */
            dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    psychologist = snapshot.getValue(Psychologist.class);

                    if (psychologist == null || psychologist.getEmail() == null) {
                        textViewAddPsychologistStatus.setText("Указанный email не найден");
                        return;
                    }

                    if (psychologist.getRole().equals("Спортсмен")) {
                        textViewAddPsychologistStatus.setText("Нужно указать email психолога, а не спортсмена");
                        return;
                    }

                    String psychologistUid = editTextPsychologistEmail.getText().toString().replace(".", "•");
                    String psychologistEmail = editTextPsychologistEmail.getText().toString().replace("•", ".");
                    /*
                      Если спортсмен переподписался на другого психолога
                     */
                    if (sportsman != null
                            && sportsman.getPsychologist() != null
                            && !sportsman.getPsychologist().equals("")
                            && !sportsman.getPsychologist().equals(psychologistEmail)
                    ) {
                        /*
                          То запрос к БД на удаление заявки спортсмена из списка заявок у психолога
                         */
                        AtomicBoolean noException = new AtomicBoolean(true);
                        dbUsers
                                .child(sportsman.getPsychologist().replace(".", "•"))
                                .child("requests")
                                .child(sportsmanUid)
                                .removeValue()
                                .addOnFailureListener(e -> {
                                    Toast.makeText(
                                            SportsmanProfileActivity.this,
                                            "Произошла ошибка при смене подписки на другого психолога (" + e.getMessage() + ")",
                                            Toast.LENGTH_LONG
                                    ).show();
                                    noException.set(false);
                                });
                        if (!noException.get()) {
                            return;
                        }
                    }
                    /*
                      Запрос к БД на добавление заявки спортсмена в список заявок у психолога
                     */
                    AtomicBoolean noException = new AtomicBoolean(true);
                    dbUsers
                            .child(psychologistUid)
                            .child("requests")
                            .child(sportsmanUid)
                            .setValue(sportsmanUid.replace("•", "."))
                            .addOnFailureListener(e -> {
                                Toast.makeText(
                                        SportsmanProfileActivity.this,
                                        "Произошла ошибка при добавлении заявки психологу (" + e.getMessage() + ")",
                                        Toast.LENGTH_LONG
                                ).show();
                                noException.set(false);
                            });
                    /*
                      Запрос к БД на добавление психолога спортсмену
                     */
                    dbUsers
                            .child(sportsmanUid)
                            .child("psychologist")
                            .setValue(psychologistEmail)
                            .addOnFailureListener(e -> {
                                Toast.makeText(
                                        SportsmanProfileActivity.this,
                                        "Произошла ошибка при добавлении заявки психологу (" + e.getMessage() + ")",
                                        Toast.LENGTH_LONG
                                ).show();
                                noException.set(false);
                            });
                    /*
                      Запрос к БД на установление значения true статусу заявки спортсмена
                     */
                    dbUsers
                            .child(sportsmanUid)
                            .child("requestAccepted")
                            .setValue(false)
                            .addOnFailureListener(e -> {
                                Toast.makeText(
                                        SportsmanProfileActivity.this,
                                        "Произошла ошибка при добавлении заявки психологу (" + e.getMessage() + ")",
                                        Toast.LENGTH_LONG
                                ).show();
                                noException.set(false);
                            });
                    if (noException.get()) {
                        textViewAddPsychologistStatus.setText("Заявка отправлена");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(
                            SportsmanProfileActivity.this,
                            "Произошла ошибка при получении данных указанного психолога (" + error.getMessage() + ")",
                            Toast.LENGTH_LONG
                    ).show();
                }
            });
        });

        /*
          Привязка к кнопке функции удаления психолога
         */
        buttonDeletePsychologist.setOnClickListener(unused4 -> {
            /*
              Создание, настройка и отображение диалогового окна подтверждения удаления
             */
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_delete_psychologist);
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams wlp = dialogWindow.getAttributes();
            wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(wlp);
            Button buttonCancelDelete = dialog.findViewById(R.id.buttonCancelDelete);
            buttonCancelDelete.setOnClickListener(unused5 -> {
                dialog.dismiss();
            });
            Button buttonConfirmDelete = dialog.findViewById(R.id.buttonConfirmDelete);
            buttonConfirmDelete.setOnClickListener(unused6 -> {
                String psychologistUid = sportsman.getPsychologist().replace(".", "•");
                /*
                  Запрос к БД на удаление психолога у спортсмена
                 */
                dbUsers
                        .child(sportsmanUid)
                        .child("psychologist")
                        .setValue("")
                        .addOnFailureListener(e -> {
                            Toast.makeText(
                                    SportsmanProfileActivity.this,
                                    "Произошла ошибка при удалении психолога (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
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
                                    SportsmanProfileActivity.this,
                                    "Произошла ошибка при добавлении удалении психолога (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
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
                                    SportsmanProfileActivity.this,
                                    "Произошла ошибка при добавлении удалении психолога (" + e.getMessage() + ")",
                                    Toast.LENGTH_LONG
                            ).show();
                        });
                dialog.dismiss();
            });
            dialog.setCancelable(false);
            dialog.show();
        });
    }
}
