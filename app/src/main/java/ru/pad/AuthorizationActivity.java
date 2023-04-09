package ru.pad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.RadioButton;

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

import ru.pad.models.User;


public class AuthorizationActivity {
    EditText editTextEmail, editTextPassword;
    Button buttonRegistration, buttonAuthorization;
    ConstraintLayout constraintLayoutActivityAuthorization;

    FirebaseDatabase database;
    FirebaseAuth auth;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegistration = findViewById(R.id.buttonRegistration);
        buttonAuthorization = findViewById(R.id.buttonAuthorization);
        constraintLayoutActivityAuthorization = findViewById(R.id.constraintLayoutActivityAuthorization);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        users = database.getReference("Users");

        buttonRegistration.setOnClickListener(view -> {
            if (TextUtils.isEmpty(editTextEmail.getText().toString())) {
                Snackbar.make(
                        constraintLayoutActivityAuthorization,
                        "Введите вашу электронную почту",
                        Snackbar.LENGTH_SHORT
                ).show();
                return;
            }

            if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
                Snackbar.make(
                        constraintLayoutActivityAuthorization,
                        "Введите пароль",
                        Snackbar.LENGTH_SHORT
                ).show();
                return;
            }


            auth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(this, PsychologistProfileActivity.class));

                        Toast.makeText(this, "Вы успешно вошли", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                })
        });
    }
}

