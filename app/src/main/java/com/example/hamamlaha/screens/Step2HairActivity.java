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

public class Step2HairActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step2hair);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // קבלת category מהIntent
        SalonCategory category = getIntent().getSerializableExtra("category", SalonCategory.class);

        // --- כפתור חזרה ---
        Button button = findViewById(R.id.btngoback);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Step2HairActivity.this, PickCategoryActivity.class);
            startActivity(intent);
        });

        // --- כפתור המשך ---
        Button button1 = findViewById(R.id.btncontinue);
        button1.setOnClickListener(view -> {

            CheckBox btnAhlaka = findViewById(R.id.btn_ahlaka);
            CheckBox btnPeen = findViewById(R.id.btn_peen);
            CheckBox btnTesporet = findViewById(R.id.btn_tesporet);
            CheckBox btnZeva = findViewById(R.id.btn_zeva);
            CheckBox btnGvanim = findViewById(R.id.btn_gvanim);
            CheckBox btnTisroket = findViewById(R.id.btn_tisroket);

            List<String> selectedOptions = new ArrayList<>();
            if (btnAhlaka.isChecked()) selectedOptions.add("החלקה");
            if (btnPeen.isChecked()) selectedOptions.add("פן");
            if (btnTesporet.isChecked()) selectedOptions.add("תספורת");
            if (btnZeva.isChecked()) selectedOptions.add("צבע");
            if (btnGvanim.isChecked()) selectedOptions.add("גוונים");
            if (btnTisroket.isChecked()) selectedOptions.add("תסרוקת");

            if (selectedOptions.isEmpty()) {
                Toast.makeText(this, "יש לבחור לפחות אופציה אחת", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = selectedOptions.size();
            String options = String.join(", ", selectedOptions);

            Intent intent = new Intent(Step2HairActivity.this, Step3Activity.class);
            intent.putExtra("category", category);
            intent.putExtra("options", options);
            intent.putExtra("duration", duration);
            startActivity(intent);
        });
    }
}