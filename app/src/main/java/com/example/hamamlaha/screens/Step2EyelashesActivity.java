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

public class Step2EyelashesActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step2eyelashes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SalonCategory category = getIntent().getSerializableExtra("category", SalonCategory.class);

        // --- כפתור חזרה ---
        Button button = findViewById(R.id.btngoback);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(Step2EyelashesActivity.this, PickCategoryActivity.class);
            startActivity(intent);
        });

        // --- כפתור המשך ---
        Button button1 = findViewById(R.id.btncontinue);
        button1.setOnClickListener(view -> {

            CheckBox btnAdbakatRisim = findViewById(R.id.btn_adbakatRisim);
            CheckBox btnAramatRisim = findViewById(R.id.btn_aramatRisim);

            List<String> selectedOptions = new ArrayList<>();
            if (btnAdbakatRisim.isChecked()) selectedOptions.add("הדבקת ריסים");
            if (btnAramatRisim.isChecked()) selectedOptions.add("הרמת ריסים");

            if (selectedOptions.isEmpty()) {
                Toast.makeText(this, "יש לבחור לפחות אופציה אחת", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = selectedOptions.size();
            String options = String.join(", ", selectedOptions);

            Intent intent = new Intent(Step2EyelashesActivity.this, Step3Activity.class);
            intent.putExtra("category", category);
            intent.putExtra("options", options);
            intent.putExtra("duration", duration);
            startActivity(intent);
        });
    }
}