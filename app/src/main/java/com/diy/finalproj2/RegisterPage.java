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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterPage extends AppCompatActivity {

    Button registerButton;
    EditText emailReg, passwordReg;
    FirebaseAuth mAuth;
    TextView loginLanding;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        emailReg = findViewById(R.id.emailReg_txtBox);
        passwordReg = findViewById(R.id.passwordReg_txtBox);
        registerButton = findViewById(R.id.buttonRegister);
        loginLanding = findViewById(R.id.loginLanding);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });

        loginLanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void createUser() {
        String email = emailReg.getText().toString();
        String password = passwordReg.getText().toString();

        if(TextUtils.isEmpty(email)){
            emailReg.setError("Missing Email");
            emailReg.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            passwordReg.setError("Missing Password");
            passwordReg.requestFocus();
        }else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterPage.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterPage.this, LoginPage.class));
                    }else{
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException existEmail) {
                            emailReg.setError("Email already exists");
                            emailReg.requestFocus();
                        } catch (Exception e) {
                        Toast.makeText(RegisterPage.this, "Registraion Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show()                    ;
                    }
                }
                    }
            });
        }
    }

}