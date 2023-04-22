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

import ru.pad.objects.Psychologist;
import ru.pad.objects.Sportsman;

public class SportsmanProfileActivity extends AppCompatActivity {
    private TextView textViewSportsmanNameSurname, textViewSportsmanBirthDate,
            textViewAddPsychologistStatus, textViewCurrentPsychologistNameSurname;
    private EditText editTextPsychologistEmail;
    private LinearLayout linearLayoutAddPsychologist, linearLayoutCurrentPsychologistNameSurname;
    private Button buttonDeletePsychologist;

    private String uid;

    private FirebaseAuth auth;
    private DatabaseReference dbUsers;

    private Psychologist psychologist;
    private Sportsman sportsman;

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
        buttonDeletePsychologist = findViewById(R.id.buttonDeletePsychologist);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbUsers = database.getReference("Users");
        auth = FirebaseAuth.getInstance();

        DatabaseReference dbUser = dbUsers.child(uid);
        dbUser.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sportsman = dataSnapshot.getValue(Sportsman.class);
                String sportsmanNameSurname = Objects.requireNonNull(sportsman).getName() + " " + Objects.requireNonNull(sportsman).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(sportsman).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                textViewSportsmanBirthDate.setText(sportsmanBirthDate);

                if (sportsman.getPsychologist() == null) {
                    return;
                }
                if (!sportsman.getPsychologist().equals("") && !sportsman.getRequestAccepted()
                ) {
                    textViewAddPsychologistStatus.setText("Ожидание ответа от " + sportsman.getPsychologist());
                }
                dbUsers.child(sportsman.getPsychologist().replace(".", "•")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Psychologist psychologist = snapshot.getValue(Psychologist.class);
                        if (psychologist != null
                                && psychologist.getName() != null
                                && psychologist.getSurname() != null
                        ) {
                            if (sportsman.getRequestAccepted()) {
                                linearLayoutAddPsychologist.setVisibility(View.GONE);
                                editTextPsychologistEmail.setVisibility(View.GONE);
                                textViewAddPsychologistStatus.setVisibility(View.GONE);
                                textViewCurrentPsychologistNameSurname.setText(psychologist.getName() + " " + psychologist.getSurname());
                                textViewCurrentPsychologistNameSurname.setVisibility(View.VISIBLE);
                                linearLayoutCurrentPsychologistNameSurname.setVisibility(View.VISIBLE);
                                buttonDeletePsychologist.setVisibility(View.VISIBLE);
                            }
                        } else {
                            linearLayoutAddPsychologist.setVisibility(View.VISIBLE);
                            editTextPsychologistEmail.setVisibility(View.VISIBLE);
                            textViewAddPsychologistStatus.setVisibility(View.VISIBLE);
                            textViewCurrentPsychologistNameSurname.setText("");
                            textViewCurrentPsychologistNameSurname.setVisibility(View.GONE);
                            linearLayoutCurrentPsychologistNameSurname.setVisibility(View.GONE);
                            buttonDeletePsychologist.setVisibility(View.GONE);
                    }
                }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
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
                    Intent calendarViewActivity = new Intent(SportsmanProfileActivity.this, CalendarViewActivity.class);
                    calendarViewActivity.putExtra("uid", uid);
                    calendarViewActivity.putExtra("role", "Спортсмен");
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.notes:
                    Intent noteWritingActivity = new Intent(SportsmanProfileActivity.this, NoteWritingActivity.class);
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
                    psychologist = snapshot.getValue(Psychologist.class);

                    if (psychologist == null || psychologist.getEmail() == null) {
                        textViewAddPsychologistStatus.setText("Указанный email не найден");
                        return;
                    }

                    String psychologistUid = editTextPsychologistEmail.getText().toString().replace(".", "•");
                    String psychologistEmail = editTextPsychologistEmail.getText().toString().replace("•", ".");
                    if (sportsman != null
                            && sportsman.getPsychologist() != null
                            && !sportsman.getPsychologist().equals("")
                            && !sportsman.getPsychologist().equals(psychologistEmail)
                    ) {
                        dbUsers
                                .child(sportsman.getPsychologist().replace(".", "•"))
                                .child("requests")
                                .child(uid)
                                .removeValue();
                    }
                    dbUsers
                            .child(psychologistUid)
                            .child("requests")
                            .child(uid)
                            .setValue(uid.replace("•", "."));
                    dbUsers
                            .child(uid)
                            .child("psychologist")
                            .setValue(psychologistEmail);
                    dbUsers
                            .child(uid)
                            .child("requestAccepted")
                            .setValue(false);
                    textViewAddPsychologistStatus.setText("Заявка отправлена");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        });

        buttonDeletePsychologist.setOnClickListener(unused4 -> {
            // TODO: удаление психолога
        });
    }
}
