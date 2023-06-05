package ru.pad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button buttonForgotPassword;
    private Button buttonAuthorizationForgotPassword;
    private EditText txtEmail;
    private String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        auth = FirebaseAuth.getInstance();

        txtEmail = findViewById(R.id.EditTextForEmail);
        buttonForgotPassword = findViewById(R.id.buttonForgotPassword);
        buttonAuthorizationForgotPassword = findViewById(R.id.ButtonAuthorizationForgotPassword);

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

        buttonAuthorizationForgotPassword.setOnClickListener(view -> {
            startActivity(new Intent(this, AuthorizationActivity.class));
            finishAffinity();
            finish();
        });
    }

    public static int dataIsValid(String email) {
        if (email == null || email.isEmpty())
            return -1;
        if (!Utils.emailFormatIsValid(email))
            return -2;
        return 0;
    }

    private void validateData() {
        switch (dataIsValid(txtEmail.getText().toString())) {
            case -1:
                txtEmail.setError("Введите вашу электронную почту");
                break;
            case -2:
                txtEmail.setError("Некорректный формат электронной почты");
                break;
            default:
                forgotPassword();
        }
    }

    private void forgotPassword() {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Проверьте свою электронную почту", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, AuthorizationActivity.class));
                            finishAffinity();
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
