package ru.pad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/* Файл с кодом всех функций окна приложения, тут принято эти окна называть через "activity" */
public class RegistrationActivity extends AppCompatActivity {

    ImageView applicationIcon;
    TextView textViewRegistration;
    EditText editTextPersonName;
    EditText editTextPersonSurname;
    EditText editTextPersonBirthDate;
    EditText editTextLogin;
    EditText editTextPassword;
    EditText editTextRepeatPassword;
    RadioButton radioButtonSportsman;
    RadioButton radioButtonPsychologist;
    Button buttonRegistration;
    Button buttonAuthorization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        applicationIcon = findViewById(R.id.appIcon);
        textViewRegistration = findViewById(R.id.textViewRegistration);
        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextPersonSurname = findViewById(R.id.editTextPersonSurname);
        editTextPersonBirthDate = findViewById(R.id.editTextPersonBirthDate);
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        radioButtonSportsman = findViewById(R.id.radioButtonSportsman);
        radioButtonPsychologist = findViewById(R.id.radioButtonPsychologist);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonAuthorization = findViewById(R.id.buttonAuthorization);
    }
}