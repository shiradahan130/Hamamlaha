package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.utils.SharedPreferencesUtil;

public class LandingActivity extends BaseActivity {

    private static final String TAG = "DEBUG_LANDING";

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "super.onCreate done");
        setContentView(R.layout.activity_landing);
        Log.d(TAG, "setContentView done");

        user = SharedPreferencesUtil.getUser(LandingActivity.this);
        Log.d(TAG, "user: " + user);
        Log.d(TAG, "isUserLoggedIn: " + SharedPreferencesUtil.isUserLoggedIn(LandingActivity.this));

        if (SharedPreferencesUtil.isUserLoggedIn(LandingActivity.this)) {
            Log.d(TAG, "user is logged in - navigating...");
            Intent intent;
            if (user.isAdmin()) {
                Log.d(TAG, "navigating to AdminActivity");
                intent = new Intent(LandingActivity.this, AdminActivity.class);
            } else {
                Log.d(TAG, "navigating to MainActivity2");
                intent = new Intent(LandingActivity.this, MainActivity2.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }

        Log.d(TAG, "user not logged in - showing landing screen");
        Button login = findViewById(R.id.login);
        Button singUp = findViewById(R.id.signUp);
        login.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        singUp.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}