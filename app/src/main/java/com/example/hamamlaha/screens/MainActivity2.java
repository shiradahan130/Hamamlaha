package com.example.hamamlaha.screens;

import android.content.ActivityNotFoundException;
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

public class MainActivity2 extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView bnt_map = findViewById(R.id.bnt_map); // המפה

        // לחיצה על המפה פותחת Waze עם הכתובת באילת
        bnt_map.setOnClickListener(v -> openWaze("שדרות ששת הימים 326, אילת"));
    }

    // פונקציה לפתיחת Waze או גרסת ה-Web שלה
    private void openWaze(String destination) {
        String encodedDestination = Uri.encode(destination);
        String wazeAppUri = "waze://?q=" + encodedDestination + "&navigate=yes";
        String wazeWebUrl = "https://www.waze.com/ul?q=" + encodedDestination + "&navigate=yes";

        try {
            // ניסיון לפתוח את האפליקציה
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(wazeAppUri));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // אם האפליקציה לא מותקנת - פותח את הניווט בדפדפן
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(wazeWebUrl));
            startActivity(intent);
        }
    }
}