package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.example.hamamlaha.utils.Validator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private Button btnLogin, btngoback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        /// get the views
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_login);
        btngoback = findViewById(R.id.goBack);

        /// set listeners
        btnLogin.setOnClickListener(this);

        btngoback.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == btnLogin.getId()) {

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            if (!checkInput(email, password)) {
                return;
            }

            loginUser(email, password);
        }
    }


    /// Validate input
    private boolean checkInput(String email, String password) {

        if (!Validator.isEmailValid(email)) {
            etEmail.setError("Invalid email address");
            etEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            etPassword.setError("Password must be at least 6 characters long");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }


    /// Login function
    private void loginUser(String email, String password) {

        DatabaseService.getInstance().getUserByEmailAndPassword(email, password,
                new DatabaseService.DatabaseCallback<User>() {
                    @Override
                    public void onCompleted(User user) {
                        if (user == null) {
                            Toast.makeText(LoginActivity.this, "לא קיים", Toast.LENGTH_LONG).show();
                            return;
                        }

                        SharedPreferencesUtil.saveUser(LoginActivity.this, user);

                        Intent mainIntent =
                                new Intent(LoginActivity.this, MainActivity2.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        etPassword.setError("Invalid email or password");
                        etPassword.requestFocus();
                        SharedPreferencesUtil.signOutUser(LoginActivity.this);
                    }
                });
    }
}