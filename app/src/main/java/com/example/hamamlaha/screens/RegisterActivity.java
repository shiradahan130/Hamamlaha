package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;

import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.example.hamamlaha.models.User;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.example.hamamlaha.utils.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etEmail, etPassword, etFName, etLName, etPhone;
    private Button btnRegister, btngoback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);
        etFName = findViewById(R.id.et_register_first_name);
        etLName = findViewById(R.id.et_register_last_name);
        etPhone = findViewById(R.id.et_register_phone);
        btnRegister = findViewById(R.id.btnRegister);
        btngoback = findViewById(R.id.goBack);


        btnRegister.setOnClickListener(this);

        btngoback.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LandingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == btnRegister.getId()) {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String fName = etFName.getText().toString();
            String lName = etLName.getText().toString();
            String phone = etPhone.getText().toString();


            if (!checkInput(email, password, fName, lName, phone)) {
                return;
            }


            registerUser(email, password, fName, lName, phone);
        }
    }

    private boolean checkInput(String email, String password, String fName, String lName, String phone) {
        if (!Validator.isNameValid(fName)) {
            etFName.setError("First name must be at least 3 characters long");
            etFName.requestFocus();
            return false;
        }

        if (!Validator.isNameValid(lName)) {
            etLName.setError("Last name must be at least 3 characters long");
            etLName.requestFocus();
            return false;
        }

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

        if (!Validator.isPhoneValid(phone)) {
            etPhone.setError("Phone number must be at least 10 characters long");
            etPhone.requestFocus();
            return false;
        }

        return true;
    }

    /// Register the user
    private void registerUser(String email, String password, String fName, String lName, String phone) {
        String uid = DatabaseService.getInstance().generateUserId();

        /// create a new user object
        User user = new User(fName,lName, uid, email, password, phone, false);

        DatabaseService.getInstance().checkIfEmailExists(email, new DatabaseService.DatabaseCallback<Boolean>() {
            @Override
            public void onCompleted(Boolean exists) {
                if (exists) {
                    Toast.makeText(RegisterActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                } else {
                    createUserInDatabase(user);
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createUserInDatabase(User user) {
        DatabaseService.getInstance().createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                SharedPreferencesUtil.saveUser(RegisterActivity.this, user);
                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                SharedPreferencesUtil.signOutUser(RegisterActivity.this);
            }
        });
    }
}