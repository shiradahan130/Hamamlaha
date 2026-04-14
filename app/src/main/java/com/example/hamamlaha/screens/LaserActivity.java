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
import androidx.viewpager2.widget.ViewPager2;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class LaserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_laser);
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
            Intent intent = new Intent(LaserActivity.this, MainActivity.class);
            startActivity(intent);
        });

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        List<Integer> images = new ArrayList<>();
        images.add(R.drawable.laser1);
        images.add(R.drawable.laser2);
        images.add(R.drawable.laser3);
        images.add(R.drawable.laser4);
        images.add(R.drawable.laser5);

        ImageAdapter adapter = new ImageAdapter(images);
        viewPager.setAdapter(adapter);

        viewPager.setClipToPadding(false);
        viewPager.setClipChildren(false);
        viewPager.setOffscreenPageLimit(3);

        viewPager.setPadding(100, 0, 100, 0);

        viewPager.setPageTransformer((page, position) -> {
            float scale = 0.8f + (1 - Math.abs(position)) * 0.2f;
            page.setScaleY(scale);
        });

        viewPager.setCurrentItem(Integer.MAX_VALUE / 2, false);
    }
}