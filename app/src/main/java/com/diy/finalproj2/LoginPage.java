package com.diy.finalproj2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class LoginPage extends AppCompatActivity {

    FirebaseAuth auth;
    ProgressDialog progressDialog;

    Button loginButton;
    EditText emailLogin, passwordLogin;
    FirebaseAuth mAuth;
    TextView registerLanding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        emailLogin = findViewById(R.id.email_txtBox);
        passwordLogin = findViewById(R.id.password_txtBox);
        loginButton = findViewById(R.id.buttonLogin);
        registerLanding = findViewById(R.id.registerLanding);

        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        registerLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        String email = emailLogin.getText().toString();
        String password = passwordLogin.getText().toString();

        if (TextUtils.isEmpty(email)) {
            emailLogin.setError("Missing Email");
            emailLogin.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            passwordLogin.setError("Missing Password");
            passwordLogin.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginPage.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginPage.this, MainActivity.class));
                    } else {
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException invalidEmail) {
                            emailLogin.setError("Email is not registered");
                            emailLogin.requestFocus();

                        } catch (FirebaseAuthInvalidCredentialsException wrongPassword) {
                            passwordLogin.setError("Incorrect Password");
                            passwordLogin.requestFocus();

                        } catch (Exception e) {
                            Toast.makeText(LoginPage.this, "Login Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        }
}
