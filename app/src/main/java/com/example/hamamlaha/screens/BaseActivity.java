package com.example.hamamlaha.screens;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.hamamlaha.R;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.google.android.material.navigation.NavigationView;

import android.graphics.drawable.Drawable;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DatabaseService databaseService;
    protected DrawerLayout drawerLayout;

    protected boolean hasSideMenu() {
        return true;
    }

    protected boolean hasToolbar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseService = DatabaseService.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(0xFFff0084);
        }

        super.setContentView(R.layout.activity_base);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        if (!hasToolbar()) {
            toolbar.setVisibility(View.GONE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }

        drawerLayout = findViewById(R.id.nav_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (hasSideMenu()) {

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                getSupportActionBar().setTitle("");
            }

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this,
                    drawerLayout,
                    toolbar,
                    R.string.open_drawer,
                    R.string.close_drawer
            );
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            // כפייה של צבע לבן אחרי syncState
            Drawable menuIcon = ContextCompat.getDrawable(this, R.drawable.baseline_menu_24);
            if (menuIcon != null) {
                menuIcon = DrawableCompat.wrap(menuIcon).mutate();
                DrawableCompat.setTint(menuIcon, 0xFFFFFFFF);
                getSupportActionBar().setHomeAsUpIndicator(menuIcon);
            }

            setNavMenuFont(navigationView);

        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            navigationView.setVisibility(View.GONE);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setHomeAsUpIndicator(null);
            }
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentLayout(layoutResID);
    }

    protected void setContentLayout(int layoutResId) {
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResId, contentFrame, true);
    }

    private void setNavMenuFont(NavigationView navigationView) {
        Typeface typeface = ResourcesCompat.getFont(this, R.font.asakimboldwebfont);
        if (typeface == null) return;

        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);

            if (item.hasSubMenu()) {
                for (int j = 0; j < item.getSubMenu().size(); j++) {
                    applyFontToMenuItem(item.getSubMenu().getItem(j), typeface);
                }
            } else {
                applyFontToMenuItem(item, typeface);
            }
        }
    }

    private void applyFontToMenuItem(MenuItem item, Typeface typeface) {
        SpannableString spannableString = new SpannableString(item.getTitle());
        spannableString.setSpan(new CustomTypefaceSpan(typeface), 0,
                spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(spannableString);
    }

    private static class CustomTypefaceSpan extends MetricAffectingSpan {
        private final Typeface typeface;

        CustomTypefaceSpan(Typeface typeface) {
            this.typeface = typeface;
        }

        @Override
        public void updateDrawState(TextPaint paint) {
            paint.setTypeface(typeface);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            paint.setTypeface(typeface);
        }
    }

    protected void navigateTo(Class<?> targetActivity) {
        if (!this.getClass().equals(targetActivity)) {
            Intent intent = new Intent(this, targetActivity);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigateTo(MainActivity2.class);
        } else if (id == R.id.nav_user_profile) {
            navigateTo(UserProfileActivity.class);
        } else if (id == R.id.nav_add_tor) {
            navigateTo(PickCategoryActivity.class);
        } else if (id == R.id.nav_gallery) {
            navigateTo(MainActivity.class);
        } else if (id == R.id.nav_signOut) {
            drawerLayout.closeDrawer(GravityCompat.START);
            showLogoutDialog();
        }
        return true;
    }

    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("התנתקות")
                .setMessage("את/ה בטוח/ה שאת/ה רוצה להתנתק?")
                .setPositiveButton("כן", (dialog, which) -> {
                    SharedPreferencesUtil.signOutUser(this);
                    Intent intent = new Intent(this, LandingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("לא", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}