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

public class MainActivity2 extends AppCompatActivity {
Button bnt_gallery, btn_tor, bnt_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bnt_gallery = findViewById(R.id.bnt_gallery);
        btn_tor = findViewById(R.id.bnt_tor);
        bnt_profile = findViewById(R.id.bnt_profile);


        bnt_gallery.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, MainActivity.class);
            startActivity(intent);
        });

            btn_tor.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity2.this, PickCategoryActivity.class);
                startActivity(intent);
        });

        bnt_profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }
}

