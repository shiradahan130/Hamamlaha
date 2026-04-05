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

            Intent intent;

            switch (category) {
                case HAIR:
                    intent = new Intent(PickCategoryActivity.this, Step2HairActivity.class);
                    break;

                case NAILS:
                    intent = new Intent(PickCategoryActivity.this, Step2NailsActivity.class);
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

                case PEDICUR:
                    intent = new Intent(PickCategoryActivity.this, Step2PedicureActivity.class);
                    break;

                default:
                    intent = new Intent(PickCategoryActivity.this, MainActivity.class);
                    break;
            }

            startActivity(intent);
        });

        rvCategories.setAdapter(adapter);

    }
}
