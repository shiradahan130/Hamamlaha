package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.SalonCategory;

import java.util.ArrayList;
import java.util.List;

public class Step2LaserActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step2laser);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SalonCategory category = getIntent().getSerializableExtra("category", SalonCategory.class);

        // --- כפתור חזרה ---
        Button button = findViewById(R.id.btngoback);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Step2LaserActivity.this, PickCategoryActivity.class);
            startActivity(intent);
        });

        // --- כפתור המשך ---
        Button button1 = findViewById(R.id.btncontinue);
        button1.setOnClickListener(view -> {

            CheckBox btnYadaeem = findViewById(R.id.btn_yadaeem);
            CheckBox btnRaglaeem = findViewById(R.id.btn_raglaeem);
            CheckBox btnBetshehi = findViewById(R.id.btn_betshehi);
            CheckBox btnMefsaot = findViewById(R.id.btn_mefsaot);
            CheckBox btnBeten = findViewById(R.id.btn_beten);
            CheckBox btnGav = findViewById(R.id.btn_gav);

            List<String> selectedOptions = new ArrayList<>();
            if (btnYadaeem.isChecked()) selectedOptions.add("ידיים");
            if (btnRaglaeem.isChecked()) selectedOptions.add("רגליים");
            if (btnBetshehi.isChecked()) selectedOptions.add("בת שחי");
            if (btnMefsaot.isChecked()) selectedOptions.add("מפשעה");
            if (btnBeten.isChecked()) selectedOptions.add("בטן");
            if (btnGav.isChecked()) selectedOptions.add("גב");

            if (selectedOptions.isEmpty()) {
                Toast.makeText(this, "יש לבחור לפחות אופציה אחת", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = selectedOptions.size();
            String options = String.join(", ", selectedOptions);

            Intent intent = new Intent(Step2LaserActivity.this, Step3Activity.class);
            intent.putExtra("category", category);
            intent.putExtra("options", options);
            intent.putExtra("duration", duration);
            startActivity(intent);
        });
    }
}