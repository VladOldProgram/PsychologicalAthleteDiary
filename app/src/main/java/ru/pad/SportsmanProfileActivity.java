package ru.pad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import ru.pad.objects.User;

public class SportsmanProfileActivity extends AppCompatActivity {
    private TextView textViewSportsmanNameSurname, textViewSportsmanBirthDate,
            textViewAddPsychologistStatus, textViewCurrentPsychologistNameSurname;
    private EditText editTextPsychologistEmail;
    private LinearLayout linearLayoutAddPsychologist, linearLayoutCurrentPsychologistNameSurname;

    private String uid;

    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference dbUsers;

    private User psychologist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsman_profile);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uid = extras.getString("uid");
        }

        textViewSportsmanNameSurname = findViewById(R.id.textViewSportsmanNameSurname);
        textViewSportsmanBirthDate = findViewById(R.id.textViewSportsmanBirthDate);
        textViewAddPsychologistStatus = findViewById(R.id.textViewAddPsychologistStatus);

        database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        DatabaseReference dbUser = dbUsers.child(uid);
        dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                String sportsmanNameSurname = Objects.requireNonNull(user).getName() + " " + Objects.requireNonNull(user).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(user).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                textViewSportsmanBirthDate.setText(sportsmanBirthDate);
                if (user.getPsychologist() != null) {
                    textViewAddPsychologistStatus.setText("Ожидание ответа от " + user.getPsychologist());
                }
                if (user.getRequestAccepted()) {
                    linearLayoutAddPsychologist.setVisibility(View.GONE);
                    editTextPsychologistEmail.setVisibility(View.GONE);
                    textViewAddPsychologistStatus.setVisibility(View.GONE);
                    textViewCurrentPsychologistNameSurname.setVisibility(View.VISIBLE);
                    linearLayoutCurrentPsychologistNameSurname.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initWidgets() {
        editTextPsychologistEmail = findViewById(R.id.editTextPsychologistEmail);

        linearLayoutAddPsychologist = findViewById(R.id.linearLayoutAddPsychologist);

        linearLayoutCurrentPsychologistNameSurname = findViewById(R.id.linearLayoutCurrentPsychologistNameSurname);
        textViewCurrentPsychologistNameSurname = findViewById(R.id.textViewCurrentPsychologistNameSurname);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    Intent calendarViewActivity = new Intent(getApplicationContext(), CalendarViewActivity.class);
                    calendarViewActivity.putExtra("uid", uid);
                    calendarViewActivity.putExtra("role", "Спортсмен");
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.notes:
                    Intent noteWritingActivity = new Intent(getApplicationContext(), NoteWritingActivity.class);
                    noteWritingActivity.putExtra("uid", uid);
                    startActivity(noteWritingActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.profile:
                    return true;
            }
            return false;
        });

        Button buttonSportsmanProfileExit = findViewById(R.id.buttonSportsmanProfileExit);
        buttonSportsmanProfileExit.setOnClickListener(unused1 -> {
            auth.signOut();
            Intent AuthorizationActivity = new Intent(this, AuthorizationActivity.class);
            startActivity(AuthorizationActivity);
            finish();
        });

        /*
        // Инициализируем элементы
        FloatingActionButton floatingActionButtonAddPsychologist = findViewById(R.id.floatingActionButtonAddPsychologist);

        // Добавляем слушателя нажатий по кнопке
        Context context = this;
        floatingActionButtonAddPsychologist.setOnClickListener(view -> {
            // Получаем вид с файла activity_add_email_psychologist.xml, который применим для диалогового окна
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View promptsView = layoutInflater.inflate(R.layout.activity_add_email_psychologist, null);

            // Создаем AlertDialog
            AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);

            // Настраиваем activity_add_email_psychologist.xml для нашего AlertDialog
            mDialogBuilder.setView(promptsView);

            // Настраиваем отображение поля для ввода текста в открытом диалоге
            EditText userInput = promptsView.findViewById(R.id.edit_text_add_email_psychologist);

            //Настраиваем сообщение в диалоговом окне:
            mDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                                // Вводим текст и отображаем в строке ввода на основном экране
                                textViewAddPsychologistStatus.setText("TEST");
                            })
                    .setNegativeButton("Отмена", (dialog, id) -> dialog.cancel());

            // Создаем AlertDialog
            AlertDialog alertDialog = mDialogBuilder.create();

            // и отображаем его
            alertDialog.show();
        });
        */

        FloatingActionButton floatingActionButtonAddPsychologist = findViewById(R.id.floatingActionButtonAddPsychologist);
        floatingActionButtonAddPsychologist.setOnClickListener(unused2 -> {
            psychologist = null;
            if (editTextPsychologistEmail.getText().toString().equals("")) {
                textViewAddPsychologistStatus.setText("Введите email");
                return;
            }

            DatabaseReference dbUser = dbUsers.child(editTextPsychologistEmail.getText().toString().replace(".", "•"));
            dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    psychologist = snapshot.getValue(User.class);

                    if (psychologist == null || psychologist.getEmail() == null) {
                        textViewAddPsychologistStatus.setText("Указанный email не найден");
                        return;
                    }

                    String psychologistUid = editTextPsychologistEmail.getText().toString().replace(".", "•");
                    dbUsers
                            .child(psychologistUid)
                            .child("requests")
                            .child(uid)
                            .setValue(uid.replace("•", "."));
                    dbUsers.child(uid).child("psychologist").setValue(psychologistUid.replace("•", "."));
                    dbUsers.child(uid).child("requestAccepted").setValue(false);
                    textViewAddPsychologistStatus.setText("Заявка отправлена");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        });

        Button buttonDeletePsychologist = findViewById(R.id.buttonDeletePsychologist);
        buttonDeletePsychologist.setOnClickListener(unused4 -> {
            // TODO: удаление психолога
        });
    }
}
