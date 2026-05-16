package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.utils.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DISPLAY_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Thread splashThread = new Thread(() -> {
            try {
                Thread.sleep(SPLASH_DISPLAY_TIME);
            } catch (InterruptedException ignored) {
            } finally {
                Intent intent;
                if (SharedPreferencesUtil.isUserLoggedIn(this)) {
                    Log.d(TAG, "User signed in, redirecting...");
                    if (SharedPreferencesUtil.getUser(this).isAdmin()) {
                        Log.d(TAG, "User is admin, redirecting to AdminHomeActivity");
                        intent = new Intent(SplashActivity.this, AdminHomeActivity.class);
                    } else {
                        Log.d(TAG, "User is not admin, redirecting to MainActivity2");
                        intent = new Intent(SplashActivity.this, MainActivity2.class);
                    }
                } else {
                    Log.d(TAG, "User not signed in, redirecting to LandingActivity");
                    intent = new Intent(SplashActivity.this, LandingActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        splashThread.start();
    }
}