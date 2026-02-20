package com.example.hamamlaha.screens;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.hamamlaha.R;

public class AppointmentActivity extends AppCompatActivity {

    private int currentStep = 1;
    private Button[] steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_appointment);

        // EdgeToEdge Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // חיבור כפתורי השלבים (Stepper)
        steps = new Button[]{
                findViewById(R.id.step1),
                findViewById(R.id.step2),
                findViewById(R.id.step3),
                findViewById(R.id.step4),
                findViewById(R.id.step5),
                findViewById(R.id.step6),
                findViewById(R.id.step7)
        };

        // כפתורי ניווט
        Button btnNext = findViewById(R.id.btnNext);
        Button btnBack = findViewById(R.id.btnBack);

        btnNext.setOnClickListener(v -> {
            if (currentStep < steps.length) {
                currentStep++;
                updateSteps();
            }
        });

        btnBack.setOnClickListener(v -> {
            if (currentStep > 1) {
                currentStep--;
                updateSteps();
            }
        });

        // עדכון צבעים וטעינת Fragment ראשון
        updateSteps();
    }

    /**
     * מתודה שמעדכנת את צבעי הכפתורים לפי השלב הנוכחי
     * וטוענת את ה-Fragment המתאים
     */
    private void updateSteps() {
        for (int i = 0; i < steps.length; i++) {
            if (i == currentStep - 1) {
                steps[i].setBackgroundColor(Color.WHITE);
                steps[i].setTextColor(Color.RED);
            } else {
                steps[i].setBackgroundColor(Color.RED);
                steps[i].setTextColor(Color.WHITE);
            }
        }

        loadStepFragment(currentStep);
    }

    /**
     * טוען את Fragment המתאים לפי מספר השלב
     */
    private void loadStepFragment(int step) {
        Fragment fragment;

        switch (step) {
            case 1:
                fragment = CategoryFragment.newInstance(); // השלב הראשון – בחירת קטגוריה
                break;
            default:
                fragment = StepFragment.newInstance(step);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainer, fragment)
                .commit();
    }
}