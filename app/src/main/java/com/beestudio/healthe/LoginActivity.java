package com.beestudio.healthe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailField;
    EditText passwordField;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;

    Button buttonLogin;
    Button buttonRegister;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private FirebaseUser userLogin;


    public static final String PREF_USER_FIRST_TIME = "user_first_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

//        isUserFirstTime = Boolean.valueOf(IntroUtils.readSharedSetting(MainActivity.this, PREF_USER_FIRST_TIME, "true"));
//        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);
        Intent introIntent = new Intent(LoginActivity.this, IntroActivity.class);
//        if(isUserFirstTime)
        startActivity(introIntent);

        emailField = findViewById(R.id.input_email);
        passwordField = findViewById(R.id.input_password);
        buttonRegister = findViewById(R.id.button_daftar);
        buttonLogin = findViewById(R.id.button_masuk);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(emailField.getText().toString(), passwordField.getText().toString());
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void login(String email, String pw) {

        if(!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email,pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            userLogin = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Login Activity", Toast.LENGTH_SHORT).show();
                        } else {
                            userLogin = null;
                            Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if(TextUtils.isEmpty(email)) {
            emailField.setError("Silakan Masukan Email");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        if(TextUtils.isEmpty(email)) {
            passwordField.setError("Silakan Masukan Password");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return  valid;
    }

    private void showProgressDialog() {
        if(progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setIndeterminate(true);
        }
    }

    private void hideProgressDialog() {
        if(progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
