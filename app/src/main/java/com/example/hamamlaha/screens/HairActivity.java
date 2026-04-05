package com.example.hamamlaha.screens;

import android.content.Intent;
// import android.net.Uri; // הערה: קשור לווידאו
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
// import androidx.media3.common.MediaItem; // הערה: קשור לווידאו
// import androidx.media3.common.util.UnstableApi; // הערה: קשור לווידאו
// import androidx.media3.exoplayer.ExoPlayer; // הערה: קשור לווידאו
// import androidx.media3.ui.PlayerView; // הערה: קשור לווידאו

import com.example.hamamlaha.R;

// @UnstableApi // הערה: קשור לווידאו
public class HairActivity extends AppCompatActivity {

    // הגדרת הנגנים בהערה
    /*
    private ExoPlayer player1, player2, player3, player4, player5, player6;

    private ExoPlayer setupVideo(int viewId, int rawResId) {
        PlayerView playerView = findViewById(viewId);
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setUseController(false);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + rawResId);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL);
        player.setVolume(0f);
        player.prepare();
        player.play();

        return player;
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_hair);

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

        // אתחול הנגנים בהערה
        /*
        player1 = setupVideo(R.id.videoHair,  R.raw.hairvideost);
        player2 = setupVideo(R.id.videoHair2, R.raw.hairvideost2);
        player3 = setupVideo(R.id.videoHair3, R.raw.hairvideocu);
        player4 = setupVideo(R.id.videoHair4, R.raw.hairvideocu2);
        player5 = setupVideo(R.id.videoHair5, R.raw.hairvideoti);
        player6 = setupVideo(R.id.videoHair6, R.raw.hairvideoti2);
        */

        Button button = findViewById(R.id.goBack);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(HairActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // השהיית הנגנים בהערה
        /*
        if (player1 != null) player1.pause();
        if (player2 != null) player2.pause();
        if (player3 != null) player3.pause();
        if (player4 != null) player4.pause();
        if (player5 != null) player5.pause();
        if (player6 != null) player6.pause();
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        // הפעלת הנגנים מחדש בהערה
        /*
        if (player1 != null) player1.play();
        if (player2 != null) player2.play();
        if (player3 != null) player3.play();
        if (player4 != null) player4.play();
        if (player5 != null) player5.play();
        if (player6 != null) player6.play();
        */
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // שחרור הנגנים מהזיכרון בהערה
        /*
        if (player1 != null) player1.release();
        if (player2 != null) player2.release();
        if (player3 != null) player3.release();
        if (player4 != null) player4.release();
        if (player5 != null) player5.release();
        if (player6 != null) player6.release();
        */
    }
}