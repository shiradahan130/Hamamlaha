package com.example.hamamlaha.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;

public class EyelashesActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eyelashes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // כפתור אינסטגרם
        ImageView instagramBtn = findViewById(R.id.instagramBtn);

        instagramBtn.setOnClickListener(v -> {
            String hamamlaha_eilat = "hamamlaha_eilat";

            Uri uri = Uri.parse("http://instagram.com/_u/" + hamamlaha_eilat);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.instagram.android");

            try {
                startActivity(intent);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/" + hamamlaha_eilat)));
            }
        });

        Button button = findViewById(R.id.goBack);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(EyelashesActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}