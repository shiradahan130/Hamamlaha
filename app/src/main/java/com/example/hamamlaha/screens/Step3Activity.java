package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.SalonCategory;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Step3Activity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    private CalendarView calendarView;

    SalonCategory category;
    String options;
    int duration;

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

        category = getIntent().getSerializableExtra("category", SalonCategory.class);
        options = getIntent().getStringExtra("options");
        duration = getIntent().getIntExtra("duration", 1);

        calendarView = findViewById(R.id.calendarView);

        // ✅ מניעת בחירת תאריך מהעבר
        calendarView.setMinDate(System.currentTimeMillis());

        // ✅ הגבלת תאריך מקסימלי - עד 3 חודשים קדימה
        long threeMonthsFromNow = System.currentTimeMillis() + (long)(90L * 24 * 60 * 60 * 1000);
        calendarView.setMaxDate(threeMonthsFromNow);

        // תאריך ברירת מחדל = היום
        final String[] selectedDate = {new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new java.util.Date(calendarView.getDate()))};

        // עדכון כשבוחרים תאריך
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate[0] = dayOfMonth + "/" + (month + 1) + "/" + year;
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
            Intent intent = new Intent(Step3Activity.this, Step4Activity.class);
            intent.putExtra("category", category);
            intent.putExtra("options", options);
            intent.putExtra("duration", duration);
            intent.putExtra("date", selectedDate[0]);
            startActivity(intent);
        });
    }
}