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

import com.google.android.material.snackbar.Snackbar;
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

import ru.pad.objects.User;


public class RegistrationActivity extends AppCompatActivity {
    TextView textViewPersonNameError, textViewPersonSurnameError, textViewPersonBirthDateError,
            textViewEmailError, textViewPasswordError, textViewRepeatPasswordError, textViewRadioGroupError;
    EditText editTextPersonName, editTextPersonSurname, editTextPersonBirthDate, editTextEmail,
            editTextPassword, editTextRepeatPassword;
    RadioButton radioButtonSportsman, radioButtonPsychologist;
    Button buttonRegistration, buttonAuthorization;
    ConstraintLayout constraintLayoutActivityRegistration;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference users;

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

        textViewPersonNameError = findViewById(R.id.textViewPersonNameError);
        textViewPersonSurnameError = findViewById(R.id.textViewPersonSurnameError);
        textViewPersonBirthDateError = findViewById(R.id.textViewPersonBirthDateError);
        textViewEmailError = findViewById(R.id.textViewEmailError);
        textViewPasswordError = findViewById(R.id.textViewPasswordError);
        textViewRepeatPasswordError = findViewById(R.id.textViewRepeatPasswordError);
        textViewRadioGroupError = findViewById(R.id.textViewRadioGroupError);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        users = database.getReference("Users");

        editTextPersonName.setOnClickListener(view -> textViewPersonNameError.setText(""));
        editTextPersonSurname.setOnClickListener(view -> textViewPersonSurnameError.setText(""));
        editTextPersonBirthDate.setOnClickListener(view -> textViewPersonBirthDateError.setText(""));
        editTextEmail.setOnClickListener(view -> textViewEmailError.setText(""));
        editTextPassword.setOnClickListener(view -> textViewPasswordError.setText(""));
        editTextRepeatPassword.setOnClickListener(view -> textViewRepeatPasswordError.setText(""));
        radioButtonSportsman.setOnClickListener(view -> textViewRadioGroupError.setText(""));
        radioButtonPsychologist.setOnClickListener(view -> textViewRadioGroupError.setText(""));

        buttonRegistration.setOnClickListener(view -> {
            if (TextUtils.isEmpty(editTextPersonName.getText().toString())) {
                textViewPersonNameError.setText("Введите ваше имя");
                return;
            }

            if (TextUtils.isEmpty(editTextPersonSurname.getText().toString())) {
                textViewPersonSurnameError.setText("Введите вашу фамилию");
                return;
            }

            if (TextUtils.isEmpty(editTextPersonBirthDate.getText().toString())) {
                textViewPersonBirthDateError.setText("Введите вашу дату рождения");
                return;
            }

            if (!dateFormatIsValid(editTextPersonBirthDate.getText().toString())) {
                textViewPersonBirthDateError.setText("Введите дату рождения в формате \"ДД.ММ.ГГГГ\"");
                return;
            }

            if (!dateIsNotFuture(editTextPersonBirthDate.getText().toString())) {
                textViewPersonBirthDateError.setText("Нельзя указать будущую дату в качестве даты рождения");
                return;
            }

            if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                textViewEmailError.setText("Введите вашу электронную почту");
                return;
            }

            if (!emailFormatIsValid(editTextEmail.getText().toString())) {
                textViewEmailError.setText("Некорректный формат электронной почты");
                return;
            }

            if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                textViewPasswordError.setText("Введите пароль");
                return;
            }

            if (editTextPassword.getText().toString().length() < 6) {
                textViewPasswordError.setText("Пароль не может быть короче 6 символов");
                return;
            }

            if (editTextPassword.getText().toString().length() > 12) {
                textViewPasswordError.setText("Пароль не может быть длиннее 12 символов");
                return;
            }

            if (!passwordFormatIsValid(editTextPassword.getText().toString())) {
                textViewPasswordError.setText("Пароль должен содержать минимум один символ в нижнем регистре и минимум одну цифру");
                return;
            }

            if (!editTextPassword.getText().toString().equals(editTextRepeatPassword.getText().toString())) {
                textViewRepeatPasswordError.setText("Введенные пароли не совпадают");
                return;
            }

            if (TextUtils.isEmpty(editTextRepeatPassword.getText().toString())) {
                textViewRepeatPasswordError.setText("Повторите пароль");
                return;
            }

            if (!radioButtonSportsman.isChecked() && !radioButtonPsychologist.isChecked()) {
                textViewRadioGroupError.setText("Выберите роль");
                return;
            }

            auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        String role;
                        if (radioButtonSportsman.isChecked()) {
                            role = radioButtonSportsman.getText().toString();
                        } else {
                            role = radioButtonPsychologist.getText().toString();
                        }
                        User user = new User(
                                editTextPersonName.getText().toString(),
                                editTextPersonSurname.getText().toString(),
                                editTextPersonBirthDate.getText().toString(),
                                editTextEmail.getText().toString(),
                                editTextPassword.getText().toString(),
                                role
                        );
                        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        users.child(uid)
                                .setValue(user)
                                .addOnSuccessListener(unused -> Snackbar.make(
                                        constraintLayoutActivityRegistration,
                                        "Регистрация прошла успешно!",
                                        Snackbar.LENGTH_SHORT
                                ).show());
                        if (user.getRole().equals("Спортсмен")) {
                            Intent sportsmanProfileActivity = new Intent(this, SportsmanProfileActivity.class);
                            sportsmanProfileActivity.putExtra("uid", uid);
                            startActivity(sportsmanProfileActivity);
                        } else {
                            Intent psychologistProfileActivity = new Intent(this, PsychologistProfileActivity.class);
                            psychologistProfileActivity.putExtra("uid", uid);
                            startActivity(psychologistProfileActivity);
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (Objects.requireNonNull(e.getMessage()).contains("email address is already in use")) {
                            String errorMessage = "указанный email-адрес уже зарегистрирован";
                            Snackbar.make(
                                    constraintLayoutActivityRegistration,
                                    "Ошибка регистрации: " + errorMessage,
                                    Snackbar.LENGTH_LONG
                            ).show();
                        }
                        else {
                            Snackbar.make(
                                    constraintLayoutActivityRegistration,
                                    "Ошибка регистрации: " + e.getMessage(),
                                    Snackbar.LENGTH_LONG
                            ).show();
                        }
                    });
        });
    }

}
