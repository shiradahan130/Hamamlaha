package com.example.hamamlaha.screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.hamamlaha.R;

public class HairActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair);

        // כפתור חזרה
        findViewById(R.id.goBack).setOnClickListener(v -> finish());

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