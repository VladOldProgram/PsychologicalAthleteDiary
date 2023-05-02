package ru.pad;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

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

import ru.pad.objects.Sportsman;

public class SelectedSportsmanProfileActivity  extends AppCompatActivity {
    private TextView textViewSportsmanBirthDate, textViewSportsmanNameSurname;

    private Sportsman sportsman;

    private DatabaseReference dbUsers;

    private String psychologistUid, sportsmanUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_sportsman_profile);
        getUserData();
        initWidgets();
    }

    private void getUserData() {
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
        dbUser.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sportsman = dataSnapshot.getValue(Sportsman.class);
                String sportsmanNameSurname = Objects.requireNonNull(sportsman).getName() + " " + Objects.requireNonNull(sportsman).getSurname();
                String sportsmanBirthDate = Objects.requireNonNull(sportsman).getBirthDate();
                textViewSportsmanNameSurname.setText(sportsmanNameSurname);
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                int age = Period.between(LocalDate.parse(sportsmanBirthDate, dtf), LocalDate.now()).getYears();
                textViewSportsmanBirthDate.setText(sportsmanBirthDate + " (" + age + ")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    private void initWidgets() {
        Button buttonSportsmanCalendar = findViewById(R.id.buttonSportsmanCalendar);
        buttonSportsmanCalendar.setOnClickListener(unused1 -> {
            Intent calendarViewActivity = new Intent(SelectedSportsmanProfileActivity.this, CalendarViewActivity.class);
            calendarViewActivity.putExtra("sportsmanUid", sportsmanUid);
            calendarViewActivity.putExtra("role", "Психолог");
            startActivity(calendarViewActivity);
            overridePendingTransition(0, 0);
        });

        Button buttonDeleteSportsman = findViewById(R.id.buttonDeleteSportsman);
        buttonDeleteSportsman.setOnClickListener(unused2 -> {
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
                finish();
                dialog.dismiss();
            });
            dialog.setCancelable(false);
            dialog.show();
        });
    }
}
