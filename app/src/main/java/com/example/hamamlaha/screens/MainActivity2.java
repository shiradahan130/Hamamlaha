package com.example.hamamlaha.screens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hamamlaha.R;

public class MainActivity2 extends BaseActivity {

    private static final String TAG = "DEBUG_MAIN2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate started");
        super.onCreate(savedInstanceState);
        Log.d(TAG, "super.onCreate done");
        setContentView(R.layout.activity_main2);
        Log.d(TAG, "setContentView done");

        // כפתור מפה
        ImageView bnt_map = findViewById(R.id.bnt_map);
        Log.d(TAG, "bnt_map: " + bnt_map);

        if (bnt_map != null) {
            bnt_map.setOnClickListener(v -> openMaps("שדרות ששת הימים 326, אילת"));
            Log.d(TAG, "setOnClickListener done");
        } else {
            Log.e(TAG, "bnt_map is NULL - בעיה ב-layout!");
        }

        // כפתור טלפון
        Button btnCall = findViewById(R.id.btn_call);
        if (btnCall != null) {
            btnCall.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0533866468"));
                startActivity(intent);
            });
        }

        // כפתור וואטסאפ
        Button btnWhatsapp = findViewById(R.id.btn_whatsapp);
        if (btnWhatsapp != null) {
            btnWhatsapp.setOnClickListener(v -> {
                String phone = "9720533866468";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://wa.me/" + phone));
                startActivity(intent);
            });
        }

        Log.d(TAG, "onCreate finished");
    }

    private void openMaps(String destination) {
        String encodedDestination = Uri.encode(destination);

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + encodedDestination);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(mapIntent);
        } catch (ActivityNotFoundException e) {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?q=" + encodedDestination));
            startActivity(intent);
        }
    }
}