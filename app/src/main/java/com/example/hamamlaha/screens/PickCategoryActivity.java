package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.CategoryAdapter;
import com.example.hamamlaha.models.SalonCategory;

public class PickCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Edge-to-edge (לא חובה אבל יפה)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pick_category);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ----------------------------
        // RecyclerView setup
        // ----------------------------
        RecyclerView rvCategories = findViewById(R.id.rvCategories);

        // Grid עם 2 ריבועים בשורה
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvCategories.setLayoutManager(gridLayoutManager);

        // Adapter עם callback ללחיצה
        CategoryAdapter adapter = new CategoryAdapter(category -> {
            // מה קורה כשנלחץ ריבוע
            // נעבור לשלב הבא של התרומה
            Intent intent = new Intent(PickCategoryActivity.this, MainActivity.class);
            intent.putExtra("selectedCategory", category.name()); // שולחים את שם הקטגוריה
            startActivity(intent);
        });

        rvCategories.setAdapter(adapter);


        // --- כפתור חזרה ---
        Button button = findViewById(R.id.btngoback);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(PickCategoryActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        // --- כפתור המשך ---
            Button button1 = findViewById(R.id.btncontinue);
            button1.setOnClickListener(view -> {
                Intent intent = new Intent(PickCategoryActivity.this, Step2HairActivity.class);
                startActivity(intent);
        });
    }
}
