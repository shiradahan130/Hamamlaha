package com.example.hamamlaha.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;

public class MainActivity extends BaseActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton button0 = findViewById(R.id.imageBtnhair);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, HairActivity.class);
                startActivity(intent);
            }
        });

        ImageButton button1 = findViewById(R.id.imageBtnlaser);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LaserActivity.class);
                startActivity(intent);
            }
        });

        ImageButton button2 = findViewById(R.id.imageBtnnails);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NailsActivity.class);
                startActivity(intent);
            }
        });

        ImageButton button3 = findViewById(R.id.imageBtneyebrows);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EyebrowsActivity.class);
                startActivity(intent);
            }
        });

        ImageButton button4 = findViewById(R.id.imageBtneyelashes);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EyelashesActivity.class);
                startActivity(intent);
            }
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

    }
}