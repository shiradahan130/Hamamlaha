package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;

import java.util.Calendar;

public class Step3Activity extends AppCompatActivity {
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- כפתור חזרה ---
        Button button = findViewById(R.id.btngoback);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Step3Activity.this, Step2HairActivity.class);
            startActivity(intent);
        });
        // --- כפתור המשך ---
        Button button1 = findViewById(R.id.btncontinue);
        button1.setOnClickListener(view -> {
            Intent intent = new Intent(Step3Activity.this, MainActivity2.class);
            startActivity(intent);
        });
    }
}