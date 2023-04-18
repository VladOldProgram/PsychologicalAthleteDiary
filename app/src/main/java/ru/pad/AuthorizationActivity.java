package ru.pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.pad.objects.User;


public class AuthorizationActivity extends AppCompatActivity {
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    EditText editTextEmail, editTextPassword;
    Button buttonRegistration, buttonAuthorization;
    ConstraintLayout constraintLayoutActivityAuthorization;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference dbUser;

    String uid;
    User user;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z\\d._%+-]+@[A-Z\\d.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static boolean emailFormatIsValid(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonAuthorization = findViewById(R.id.buttonAuthorization);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        constraintLayoutActivityAuthorization = findViewById(R.id.constraintLayoutActivityAuthorization);

        inputLayoutEmail = findViewById(R.id.inputLayoutEmail);
        inputLayoutPassword = findViewById(R.id.inputLayoutPassword);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        buttonAuthorization.setOnClickListener(unused1 -> {

            if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                inputLayoutEmail.setError("Введите вашу электронную почту");
                return;
            } else inputLayoutEmail.setError(null);

            if (!emailFormatIsValid(editTextEmail.getText().toString())) {
                inputLayoutEmail.setError("Некорректный формат электронной почты");
                return;
            }  else inputLayoutEmail.setError(null);

            if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                inputLayoutPassword.setError("Введите пароль");
                return;
            } inputLayoutEmail.setError(null);

            auth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnSuccessListener(unused2 -> {
                        uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                        dbUser = database.getReference("Users/" + uid);
                        dbUser.addValueEventListener(new ValueEventListener() {
                            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                user = dataSnapshot.getValue(User.class);
                                if (Objects.requireNonNull(user).getRole().equals("Спортсмен")) {
                                    Intent sportsmanProfileActivity = new Intent(AuthorizationActivity.this, SportsmanProfileActivity.class);
                                    sportsmanProfileActivity.putExtra("uid", uid);
                                    startActivity(sportsmanProfileActivity);
                                } else {
                                    Intent psychologistProfileActivity = new Intent(AuthorizationActivity.this, PsychologistProfileActivity.class);
                                    psychologistProfileActivity.putExtra("uid", uid);
                                    startActivity(psychologistProfileActivity);
                                }
                                finish();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) { }
                        });
                    })
                    .addOnFailureListener(e -> {
                        if (Objects.requireNonNull(e.getMessage()).contains("There is no user record")) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Ошибка авторизации: указанный email-адрес не зарегистрирован",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else if (Objects.requireNonNull(e.getMessage()).contains("The password is invalid")) {
                            Toast.makeText(getApplicationContext(),
                                    "Ошибка авторизации: неправильный пароль ",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else if (Objects.requireNonNull(e.getMessage()).contains("A network error")) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Ошибка авторизации: потеряно соединение с интернетом",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Ошибка авторизации: " + e.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
        });

        buttonRegistration.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
        });
    }
}
