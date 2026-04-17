package com.example.hamamlaha.screens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.hamamlaha.R;

public class MainActivity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView bnt_map = findViewById(R.id.bnt_map);
        bnt_map.setOnClickListener(v -> openMaps("שדרות ששת הימים 326, אילת"));
    }

    private void openMaps(String destination) {
        String encodedDestination = Uri.encode(destination);

        // ניסיון לפתוח Google Maps
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + encodedDestination);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            // אם Maps לא מותקן - פותח בדפדפן
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?q=" + encodedDestination));
            startActivity(intent);
        }
    }
}