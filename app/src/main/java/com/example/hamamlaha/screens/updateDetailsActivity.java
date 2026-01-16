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

public class updateDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "updateDetailsActivity";
        private EditText  etFirstName, etLastName, etEmail, etPhoneNumber, etPassword;
    private Button btnUpdateProfile;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn = findViewById(R.id.goBack);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(updateDetailsActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });


        etFirstName = findViewById(R.id.firstnameInput);
        etLastName = findViewById(R.id.lastnameInput);
        etEmail = findViewById(R.id.emailInput);
        etPhoneNumber = findViewById(R.id.phonenumberInput);
        etPassword = findViewById(R.id.passwordInput);

        // אתחול הכפתור והגדרת OnClickListener
        btnUpdateProfile = findViewById(R.id.btn_updateDetails_toHome);
        btnUpdateProfile.setOnClickListener(this);


        // קבלת המשתמש מהSharedPreferences
        currentUser = SharedPreferencesUtil.getUser(this);

        // הצגת הפרטים בשדות
        if (currentUser != null) {
            showUserProfile();
        }

    }
    private void showUserProfile() {
        etFirstName.setText(currentUser.getFname());
        etLastName.setText(currentUser.getLname());
        etEmail.setText(currentUser.getEmail());
        etPhoneNumber.setText(currentUser.getPhone());
        etPassword.setText(currentUser.getPassword());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_updateDetails_toHome) {
            updateUserProfile();
        }
    }

    private void updateUserProfile() {
        if (currentUser == null) {
            Toast.makeText(this, "משתמש לא נמצא", Toast.LENGTH_SHORT).show();
            return;
        }

        // קריאת הערכים מהשדות
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhoneNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // בדיקה שהקלט תקין
        if (!isValid(firstName, lastName, phone, email, password)) {
            return;
        }

        // עדכון האובייקט
        currentUser.setFname(firstName);
        currentUser.setLname(lastName);
        currentUser.setEmail(email);
        currentUser.setPhone(phone);
        currentUser.setPassword(password);

        // עדכון במסד הנתונים
        DatabaseService.getInstance().updateUser(currentUser, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void result) {
                Toast.makeText(updateDetailsActivity.this, "פרטייך עודכנו בהצלחה!", Toast.LENGTH_LONG).show();
                showUserProfile(); // ריענון השדות
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(updateDetailsActivity.this, "שגיאה בעדכון הפרטים", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error updating user", e);
            }
        });
    } // סוגר הסופי של updateUserProfile

    private boolean isValid(String firstName, String lastName, String phone, String email, String password) {
        if (!Validator.isNameValid(firstName)) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return false;
        }
        if (!Validator.isNameValid(lastName)) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return false;
        }
        if (!Validator.isPhoneValid(phone)) {
            etPhoneNumber.setError("Phone number is required");
            etPhoneNumber.requestFocus();
            return false;
        }
        if (!Validator.isEmailValid(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!Validator.isPasswordValid(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }
        return true;
    }

} // סוגר הסופי של המחלקה

