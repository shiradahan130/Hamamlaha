package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;

public class MainActivity extends AppCompatActivity {



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


        ImageButton button00 = findViewById(R.id.bnt_UserProfile);
        button00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
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

        ImageButton button5 = findViewById(R.id.imageBtnpedicure);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PedicureActivity.class);
                startActivity(intent);
            }
        });

    }
}