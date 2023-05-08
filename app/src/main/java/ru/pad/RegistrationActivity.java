package ru.pad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;

import ru.pad.objects.GenericUser;

/**
 * Класс объекта активности регистрации
 */
public class RegistrationActivity extends AppCompatActivity {
    /**
     * Регулярное выражение для определения корректности формата электронной почты
     */
    public static final Pattern VALID_EMAIL_REGEX =
            Pattern.compile("^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Определяет, корректный ли формат имеет электронная почта, используя регулярное выражение
     * <b>VALID_EMAIL_REGEX</b>
     * @param email электронная почта, формат которой нужно проверить
     * @return true, если формат электронной почты корректен, иначе - false
     * @see #VALID_EMAIL_REGEX
     */
    public static boolean emailFormatIsValid(String email) {
        Matcher matcher = VALID_EMAIL_REGEX.matcher(email);
        return matcher.matches();
    }

    /**
     * Регулярное выражение для определения корректности формата даты
     */
    public static final Pattern VALID_DATE_REGEX =
            Pattern.compile("^\\d{2}.\\d{2}.\\d{4}$", Pattern.CASE_INSENSITIVE);

    /**
     * Определяет, корректный ли формат имеет дата, используя регулярное выражение
     * <b>VALID_DATE_REGEX</b>
     * @param date дата, формат которой нужно проверить
     * @return true, если формат даты корректен, иначе - false
     * @see #VALID_DATE_REGEX
     */
    public static boolean dateFormatIsValid(String date) {
        Matcher matcher = VALID_DATE_REGEX.matcher(date);
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

    /**
     * Определяет, является ли дата будущей
     * @param date дата, которую нужно проверить
     * @return true, если дата является будущей, иначе - false
     */
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

    /**
     * Регулярное выражение для определения корректности формата пароля
     */
    public static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=\\S+$).{6,12}$", Pattern.CASE_INSENSITIVE);

    /**
     * Определяет, корректный ли формат имеет пароль, используя регулярное выражение
     * <b>VALID_PASSWORD_REGEX</b>
     * @param password пароль, формат которого нужно проверить
     * @return true, если формат пароля корректен, иначе - false
     * @see #VALID_PASSWORD_REGEX
     */
    public static boolean passwordFormatIsValid(String password) {
        Matcher matcher = VALID_PASSWORD_REGEX.matcher(password);
        return matcher.matches();
    }

    /**
     * Отображает эту активность на экране, инициализирует виджеты активности, реализует алгоритм регистрации пользователя
     * @param savedInstanceState сохраненное состояние активности
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView editTextPersonName = findViewById(R.id.editTextPersonName);
        TextView editTextPersonSurname = findViewById(R.id.editTextPersonSurname);
        TextView editTextPersonBirthDate = findViewById(R.id.editTextPersonBirthDate);
        TextView editTextEmail = findViewById(R.id.editTextEmail);
        TextView editTextPassword = findViewById(R.id.editTextPassword);
        TextView editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        RadioButton radioButtonSportsman = findViewById(R.id.radioButtonSportsman);
        RadioButton radioButtonPsychologist = findViewById(R.id.radioButtonPsychologist);

        TextInputLayout inputLayoutPersonName = findViewById(R.id.inputLayoutPersonName);
        TextInputLayout inputLayoutPersonSurname = findViewById(R.id.inputLayoutPersonSurname);
        TextInputLayout inputLayoutPersonBirthDate = findViewById(R.id.inputLayoutPersonBirthDate);
        TextInputLayout inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        TextInputLayout inputLayoutPassword = findViewById(R.id.inputLayoutPassword);
        TextInputLayout inputLayoutRepeatPassword = findViewById(R.id.inputLayoutRepeatPassword);
        TextView textViewRadioGroupError = findViewById(R.id.textViewRadioGroupError);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        radioButtonSportsman.setOnClickListener(view -> textViewRadioGroupError.setText(""));
        radioButtonPsychologist.setOnClickListener(view -> textViewRadioGroupError.setText(""));

        Button buttonRegistration = findViewById(R.id.buttonRegistration);
        /*
          Привязка к кнопке функции регистрации пользователя
         */
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

            /*
              Попытка регистрации пользователя
             */
            auth.createUserWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    /*
                      Если регистрация удалась, запуск следующей активности - профиля спортсмена/психолога
                     */
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
                        database.getReference("Users").child(uid).setValue(genericUser);
                        if (genericUser.getRole().equals("Спортсмен")) {
                            Intent sportsmanProfileActivity = new Intent(this, SportsmanProfileActivity.class);
                            sportsmanProfileActivity.putExtra("sportsmanUid", uid);
                            startActivity(sportsmanProfileActivity);
                        } else {
                            Intent psychologistProfileActivity = new Intent(this, PsychologistProfileActivity.class);
                            psychologistProfileActivity.putExtra("psychologistUid", uid);
                            startActivity(psychologistProfileActivity);
                        }
                        finishAffinity();
                        finish();
                    })
                    /*
                      Если регистрация не удалась, вывод пользователю соответствующего сообщения об ошибке
                     */
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

        Button buttonAuthorization = findViewById(R.id.buttonAuthorization);
        /*
          Привязка к кнопке функции перехода в активность авторизации
         */
        buttonAuthorization.setOnClickListener(view -> {
            startActivity(new Intent(this, AuthorizationActivity.class));
            finishAffinity();
            finish();
        });
    }
}
