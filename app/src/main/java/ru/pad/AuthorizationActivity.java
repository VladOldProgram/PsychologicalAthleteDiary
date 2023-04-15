package ru.pad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

import android.view.Gravity;


public class AuthorizationActivity extends AppCompatActivity {

    TextView textViewTextEmailError, textViewTextPasswordError;
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

        textViewTextEmailError = findViewById(R.id.textViewTextEmailError);
        textViewTextPasswordError = findViewById(R.id.textViewTextPasswordError);

        textViewTextEmailError.setOnClickListener(view -> textViewTextEmailError.setText(""));
        textViewTextPasswordError.setOnClickListener(view -> textViewTextPasswordError.setText(""));


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        buttonAuthorization.setOnClickListener(unused1 -> {

            if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                textViewTextEmailError.setText("Введите вашу электронную почту");
                return;
            }

            if (!emailFormatIsValid(editTextEmail.getText().toString())) {
                textViewTextEmailError.setText("Некорректный формат электронной почты");
                return;
            }

            if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                textViewTextPasswordError.setText("Введите пароль");
                return;
            }

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
                            Toast toast = Toast.makeText(getApplicationContext(), "Ошибка авторизации: указанный email-адрес не зарегистрирован", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else if (Objects.requireNonNull(e.getMessage()).contains("The password is invalid")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Ошибка авторизации: неправильный пароль ", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else if (Objects.requireNonNull(e.getMessage()).contains("A network error")) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Ошибка авторизации: потеряно соединение с интернетом", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else {
                            Toast toast = Toast.makeText(getApplicationContext(), "Ошибка авторизации: " + e.getMessage(), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
        });

        buttonRegistration.setOnClickListener(view -> {
            startActivity(new Intent(this, RegistrationActivity.class));
            finish();
        });
    }
}

