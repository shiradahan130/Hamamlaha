package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.CategoryAdapter;
import com.example.hamamlaha.models.SalonCategory;

public class PickCategoryActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pick_category);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btngoback = findViewById(R.id.btngoback);
        btngoback.setOnClickListener(v -> {
            Intent intent = new Intent(PickCategoryActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvCategories.setLayoutManager(gridLayoutManager);

        CategoryAdapter adapter = new CategoryAdapter(category -> {

            Intent intent;

            // NAILS ו-PEDICUR - דילוג על שלב 2, ישירות ליומן
            if (category == SalonCategory.NAILS || category == SalonCategory.PEDICUR) {
                intent = new Intent(PickCategoryActivity.this, Step3Activity.class);
                intent.putExtra("category", category);
                intent.putExtra("options", category.getHebrewName());
                intent.putExtra("duration", 1);
                startActivity(intent);
                return;
            }

            // שאר השירותים - עוברים לשלב 2
            switch (category) {
                case HAIR:
                    intent = new Intent(PickCategoryActivity.this, Step2HairActivity.class);
                    break;
                case EYELASHES:
                    intent = new Intent(PickCategoryActivity.this, Step2EyelashesActivity.class);
                    break;
                case LASER:
                    intent = new Intent(PickCategoryActivity.this, Step2LaserActivity.class);
                    break;
                case EYEBROWS:
                    intent = new Intent(PickCategoryActivity.this, Step2EyebrowsActivity.class);
                    break;
                default:
                    intent = new Intent(PickCategoryActivity.this, MainActivity.class);
                    break;
            }

            intent.putExtra("category", category);
            startActivity(intent);
        });

        rvCategories.setAdapter(adapter);
    }
}