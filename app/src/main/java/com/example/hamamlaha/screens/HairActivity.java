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

        // --- VideoView 2 ---
        VideoView videoView2 = findViewById(R.id.videoHair2);
        Uri uri2 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hairvideost2);
        videoView2.setVideoURI(uri2);

        videoView2.setOnPreparedListener(mp -> {
            mp.setLooping(true); // סרטון חוזר על עצמו
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            videoView2.start();
        });

        // --- VideoView 3 ---
        VideoView videoView3 = findViewById(R.id.videoHair3);
        Uri uri3 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hairvideocu);
        videoView2.setVideoURI(uri3);

        videoView2.setOnPreparedListener(mp -> {
            mp.setLooping(true); // סרטון חוזר על עצמו
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            videoView3.start();
        });

        // --- VideoView 4 ---
        VideoView videoView4 = findViewById(R.id.videoHair4);
        Uri uri4 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hairvideocu2);
        videoView2.setVideoURI(uri4);

        videoView2.setOnPreparedListener(mp -> {
            mp.setLooping(true); // סרטון חוזר על עצמו
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            videoView4.start();
        });

        // --- VideoView 5 ---
        VideoView videoView5 = findViewById(R.id.videoHair5);
        Uri uri5 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hairvideoti);
        videoView2.setVideoURI(uri5);

        videoView2.setOnPreparedListener(mp -> {
            mp.setLooping(true); // סרטון חוזר על עצמו
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            videoView5.start();
        });

        // --- VideoView 6 ---
        VideoView videoView6 = findViewById(R.id.videoHair6);
        Uri uri6 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.hairvideoti2);
        videoView2.setVideoURI(uri6);

        videoView2.setOnPreparedListener(mp -> {
            mp.setLooping(true); // סרטון חוזר על עצמו
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            }
            videoView6.start();
        });

        // --- כפתור חזרה ---
        Button button = findViewById(R.id.goBack);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(HairActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
