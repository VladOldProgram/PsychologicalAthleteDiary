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
    private TextView textViewAddPsychologistStatus, textViewCurrentPsychologistNameSurname,
            textViewSportsmanBirthDate, textViewSportsmanNameSurname;
    private EditText editTextPsychologistEmail;
    private LinearLayout linearLayoutAddPsychologist, linearLayoutCurrentPsychologistNameSurname;
    private Button buttonDeletePsychologist;

    private String sportsmanUid;

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
        dbUser.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sportsman = dataSnapshot.getValue(Sportsman.class);
                String sportsmanNameSurname = Objects.requireNonNull(sportsman).getName() + " " + Objects.requireNonNull(sportsman).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(sportsman).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                textViewSportsmanBirthDate.setText(sportsmanBirthDate);
                if (sportsman.getPsychologist() == null || sportsman.getPsychologist().equals("")) {
                    linearLayoutAddPsychologist.setVisibility(View.VISIBLE);
                    editTextPsychologistEmail.setVisibility(View.VISIBLE);
                    textViewAddPsychologistStatus.setVisibility(View.VISIBLE);
                    textViewCurrentPsychologistNameSurname.setText("");
                    textViewCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    linearLayoutCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    buttonDeletePsychologist.setVisibility(View.GONE);
                    return;
                }
                if (!sportsman.getRequestAccepted()) {
                    textViewAddPsychologistStatus.setText("Ожидание ответа от " + sportsman.getPsychologist());
                    linearLayoutAddPsychologist.setVisibility(View.VISIBLE);
                    editTextPsychologistEmail.setVisibility(View.VISIBLE);
                    textViewAddPsychologistStatus.setVisibility(View.VISIBLE);
                    textViewCurrentPsychologistNameSurname.setText("");
                    textViewCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    linearLayoutCurrentPsychologistNameSurname.setVisibility(View.GONE);
                    buttonDeletePsychologist.setVisibility(View.GONE);
                    return;
                }
                dbUsers.child(sportsman.getPsychologist().replace(".", "•")).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Psychologist psychologist = snapshot.getValue(Psychologist.class);
                        if (psychologist != null
                                && psychologist.getName() != null
                                && psychologist.getSurname() != null
                        ) {
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
                    public void onCancelled(@NonNull DatabaseError error) { }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    @SuppressLint({"SetTextI18n", "NonConstantResourceId"})
    private void initWidgets() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.calendar:
                    Intent calendarViewActivity = new Intent(SportsmanProfileActivity.this, CalendarViewActivity.class);
                    calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
                    calendarViewActivity.putExtra("role", "Спортсмен");
                    startActivity(calendarViewActivity);
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.notes:
                    Intent noteWritingActivity = new Intent(SportsmanProfileActivity.this, NoteWritingActivity.class);
                    noteWritingActivity.putExtra("sportsmanUid", sportsmanUid);
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
                                .child(sportsmanUid)
                                .removeValue();
                    }
                    dbUsers
                            .child(psychologistUid)
                            .child("requests")
                            .child(sportsmanUid)
                            .setValue(sportsmanUid.replace("•", "."));
                    dbUsers
                            .child(sportsmanUid)
                            .child("psychologist")
                            .setValue(psychologistEmail);
                    dbUsers
                            .child(sportsmanUid)
                            .child("requestAccepted")
                            .setValue(false);
                    textViewAddPsychologistStatus.setText("Заявка отправлена");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        });

        buttonDeletePsychologist.setOnClickListener(unused4 -> {
            String psychologistUid = sportsman.getPsychologist().replace(".", "•");
            dbUsers
                    .child(sportsmanUid)
                    .child("psychologist")
                    .setValue("");
            dbUsers
                    .child(sportsmanUid)
                    .child("requestAccepted")
                    .setValue(false);
            dbUsers
                    .child(psychologistUid)
                    .child("sportsmen")
                    .child(sportsmanUid)
                    .removeValue();
        });
    }
}
