package com.example.hamamlaha.screens;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;

public class HairActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hair);

        // --- EdgeToEdge padding ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- VideoView ---
        VideoView videoView = findViewById(R.id.videoHair);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hairvideost);
        videoView.setVideoURI(uri);

        // --- CenterCrop + Looping ---
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true); // סרטון חוזר על עצמו
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            videoView.start();
        });

        // --- כפתור חזרה ---
        Button button = findViewById(R.id.goBack);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(HairActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
