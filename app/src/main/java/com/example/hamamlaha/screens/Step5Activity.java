package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.SalonCategory;

public class Step5Activity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // קבלת פרטי התור
        SalonCategory category = getIntent().getSerializableExtra("category", SalonCategory.class);
        String options = getIntent().getStringExtra("options");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        int duration = getIntent().getIntExtra("duration", 1);

        // הצגת הפרטים
        TextView tvCategory = findViewById(R.id.tv_category);
        TextView tvOptions = findViewById(R.id.tv_options);
        TextView tvDate = findViewById(R.id.tv_date);
        TextView tvTime = findViewById(R.id.tv_time);

        tvCategory.setText(category != null ? category.name() : "");
        tvOptions.setText(options);
        tvDate.setText(date);

        // ✅ חישוב שעת סיום והצגת "ממתי עד מתי"
        tvTime.setText(calcTimeRange(time, duration));

        // כפתור חזרה לדף הבית
        Button btnHome = findViewById(R.id.btn_home);
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(Step5Activity.this, MainActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    // ✅ מחשב טווח שעות בפורמט "HH:mm - HH:mm"
    private String calcTimeRange(String startTime, int duration) {
        if (startTime == null || startTime.isEmpty()) return "";

        try {
            String[] parts = startTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            int endHour = (hour + duration) % 24;

            return String.format("%02d:%02d - %02d:%02d", hour, minute, endHour, minute);
        } catch (Exception e) {
            return startTime;
        }
    }
}