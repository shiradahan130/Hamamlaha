package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.utils.SharedPreferencesUtil;

public class LandingActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_landing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user= SharedPreferencesUtil.getUser(LandingActivity.this);
        if(SharedPreferencesUtil.isUserLoggedIn(LandingActivity.this)) {
            Intent intent = new Intent(LandingActivity.this, MainActivity2.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        Button login=findViewById(R.id.login);
        Button singUp = findViewById(R.id.signUp);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent=new Intent(LandingActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        singUp.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View view){
                  Intent intent=new Intent(LandingActivity.this, LoginActivity.class);
                  startActivity(intent);
              }
          }
        );
    }
}