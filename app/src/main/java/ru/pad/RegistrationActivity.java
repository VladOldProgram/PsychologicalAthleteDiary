package ru.pad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import ru.pad.objects.GenericUser;
import ru.pad.objects.Sportsman;


public class RegistrationActivity extends AppCompatActivity {
    TextInputLayout inputLayoutPersonName, inputLayoutPersonSurname, inputLayoutPersonBirthDate,
            inputLayoutEmail, inputLayoutPassword, inputLayoutRepeatPassword;
    TextView textViewRadioGroupError;
    EditText editTextPersonName, editTextPersonSurname, editTextPersonBirthDate, editTextEmail,
            editTextPassword, editTextRepeatPassword;
    RadioButton radioButtonSportsman, radioButtonPsychologist;
    Button buttonRegistration, buttonAuthorization;
    ConstraintLayout constraintLayoutActivityRegistration;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference dbUsers;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean emailFormatIsValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    public static final Pattern VALID_DATE_ADDRESS_REGEX =
            Pattern.compile("^\\d{2}.\\d{2}.\\d{4}$", Pattern.CASE_INSENSITIVE);
    public static boolean dateFormatIsValid(String date) {
        Matcher matcher = VALID_DATE_ADDRESS_REGEX.matcher(date);
        if (!matcher.matches()) {
            return false;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        df.setLenient(false);
        try {
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean dateIsNotFuture(String date) {
        try {
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.ROOT);
            Date registrationDate = formatter.parse(date);
            if (currentDate.before(registrationDate)) {
                return false;
            }
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static final Pattern VALID_PASSWORD_ADDRESS_REGEX =
            Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=\\S+$).{6,12}$", Pattern.CASE_INSENSITIVE);
    public static boolean passwordFormatIsValid(String password) {
        Matcher matcher = VALID_PASSWORD_ADDRESS_REGEX.matcher(password);
        return matcher.matches();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextPersonName = findViewById(R.id.editTextPersonName);
        editTextPersonSurname = findViewById(R.id.editTextPersonSurname);
        editTextPersonBirthDate = findViewById(R.id.editTextPersonBirthDate);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        radioButtonSportsman = findViewById(R.id.radioButtonSportsman);
        radioButtonPsychologist = findViewById(R.id.radioButtonPsychologist);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonAuthorization = findViewById(R.id.buttonAuthorization);
        constraintLayoutActivityRegistration = findViewById(R.id.constraintLayoutActivityRegistration);

        inputLayoutPersonName = findViewById(R.id.inputLayoutPersonName);
        inputLayoutPersonSurname = findViewById(R.id.inputLayoutPersonSurname);
        inputLayoutPersonBirthDate = findViewById(R.id.inputLayoutPersonBirthDate);
        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        inputLayoutRepeatPassword = findViewById(R.id.inputLayoutRepeatPassword);
        textViewRadioGroupError = findViewById(R.id.textViewRadioGroupError);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        dbUsers = database.getReference("Users");

        radioButtonSportsman.setOnClickListener(view -> textViewRadioGroupError.setText(""));
        radioButtonPsychologist.setOnClickListener(view -> textViewRadioGroupError.setText(""));

        buttonRegistration.setOnClickListener(unused1 -> {
            if (TextUtils.isEmpty(editTextPersonName.getText().toString())) {
                inputLayoutPersonName.setError("Введите ваше имя");
                return;
            } else inputLayoutPersonName.setError(null);

            if (TextUtils.isEmpty(editTextPersonSurname.getText().toString())) {
                inputLayoutPersonSurname.setError("Введите вашу фамилию");
                return;
            } else inputLayoutPersonSurname.setError(null);

            if (TextUtils.isEmpty(editTextPersonBirthDate.getText().toString())) {
                inputLayoutPersonBirthDate.setError("Введите вашу дату рождения");
                return;
            } else inputLayoutPersonBirthDate.setError(null);

            if (!dateFormatIsValid(editTextPersonBirthDate.getText().toString())) {
                inputLayoutPersonBirthDate.setError("Введите дату рождения в формате \"ДД.ММ.ГГГГ\"");
                return;
            } else inputLayoutPersonBirthDate.setError(null);

            if (!dateIsNotFuture(editTextPersonBirthDate.getText().toString())) {
                inputLayoutPersonBirthDate.setError("Нельзя указать будущую дату в качестве даты рождения");
                return;
            } else inputLayoutPersonBirthDate.setError(null);

            if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                inputLayoutEmail.setError("Введите вашу электронную почту");
                return;
            } else inputLayoutEmail.setError(null);

            if (!emailFormatIsValid(editTextEmail.getText().toString())) {
                inputLayoutEmail.setError("Некорректный формат электронной почты");
                return;
            } else inputLayoutEmail.setError(null);

            if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                inputLayoutPassword.setError("Введите пароль");
                return;
            } else inputLayoutPassword.setError(null);

            if (editTextPassword.getText().toString().length() < 6) {
                inputLayoutPassword.setError("Пароль не может быть короче 6 символов");
                return;
            } else inputLayoutPassword.setError(null);

            if (editTextPassword.getText().toString().length() > 12) {
                inputLayoutPassword.setError("Пароль не может быть длиннее 12 символов");
                return;
            } else inputLayoutPassword.setError(null);

            if (!passwordFormatIsValid(editTextPassword.getText().toString())) {
                inputLayoutPassword.setError("Пароль должен содержать минимум один символ в нижнем регистре и минимум одну цифру");
                return;
            } else inputLayoutPassword.setError(null);

            if (!editTextPassword.getText().toString().equals(editTextRepeatPassword.getText().toString())) {
                inputLayoutRepeatPassword.setError("Введенные пароли не совпадают");
                return;
            } else inputLayoutRepeatPassword.setError(null);

            if (TextUtils.isEmpty(editTextRepeatPassword.getText().toString())) {
                inputLayoutRepeatPassword.setError("Повторите пароль");
                return;
            } else inputLayoutRepeatPassword.setError(null);

            if (!radioButtonSportsman.isChecked() && !radioButtonPsychologist.isChecked()) {
                textViewRadioGroupError.setText("Выберите роль");
                return;
            }

            auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnSuccessListener(unused2 -> {
                        String role;
                        if (radioButtonSportsman.isChecked()) {
                            role = radioButtonSportsman.getText().toString();
                        } else {
                            role = radioButtonPsychologist.getText().toString();
                        }
                        GenericUser genericUser = new GenericUser(
                                editTextPersonName.getText().toString(),
                                editTextPersonSurname.getText().toString(),
                                editTextPersonBirthDate.getText().toString(),
                                editTextEmail.getText().toString(),
                                editTextPassword.getText().toString(),
                                role
                        );
                        String uid = genericUser.getEmail().replace(".", "•");
                        dbUsers.child(uid).setValue(genericUser);
                        if (genericUser.getRole().equals("Спортсмен")) {
                            Intent sportsmanProfileActivity = new Intent(this, SportsmanProfileActivity.class);
                            sportsmanProfileActivity.putExtra("sportsmanUid", uid);
                            startActivity(sportsmanProfileActivity);
                        } else {
                            Intent psychologistProfileActivity = new Intent(this, PsychologistProfileActivity.class);
                            psychologistProfileActivity.putExtra("psychologistUid", uid);
                            startActivity(psychologistProfileActivity);
                        }
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        if (Objects.requireNonNull(e.getMessage()).contains("email address is already in use")) {
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Ошибка регистрации: указанный email-адрес уже зарегистрирован",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else if (Objects.requireNonNull(e.getMessage()).contains("A network error")) {
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Ошибка регистрации: потеряно соединение с интернетом",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else {
                            Toast.makeText(
                                    RegistrationActivity.this,
                                    "Ошибка регистрации:" + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });

        buttonAuthorization.setOnClickListener(view -> {
            startActivity(new Intent(this, AuthorizationActivity.class));
            finish();
        });
    }

}
