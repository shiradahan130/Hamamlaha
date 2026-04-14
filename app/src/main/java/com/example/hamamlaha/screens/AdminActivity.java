package com.example.hamamlaha.screens;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.AppointmentAdminAdapter;
import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class AdminActivity extends AppCompatActivity {

    private AppointmentAdminAdapter adapter;
    private User currentUser;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // הגדרת Toolbar
        Toolbar toolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);

        // הסתרת הכותרת
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // הגדרת Drawer
        drawerLayout = findViewById(R.id.admin_drawer_layout);
        NavigationView adminNavView = findViewById(R.id.admin_nav_view);

        // הגדרת פונט לפריטי התפריט
        setNavMenuFont(adminNavView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // לחיצות על התפריט
        adminNavView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_appointments) {
                // כבר בדף הזה
            } else if (id == R.id.nav_manage_users) {
                startActivity(new Intent(AdminActivity.this, UsersListActivity.class));
            } else if (id == R.id.nav_logout) {
                drawerLayout.closeDrawers();
                showLogoutDialog();
            }
            drawerLayout.closeDrawers();
            return true;
        });

        // הגדרת RecyclerView
        RecyclerView rv = findViewById(R.id.rv_appointments);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdminAdapter(this, new ArrayList<>(), new AppointmentAdminAdapter.OnAppointmentActionListener() {
            @Override
            public void onApprove(Appointment appointment) {
                appointment.setStatus("APPROVED");
                DatabaseService.getInstance().createNewAppointment(appointment, null);
                loadAppointments();
            }

            @Override
            public void onCancel(Appointment appointment) {
                appointment.setStatus("CANCELED");
                DatabaseService.getInstance().createNewAppointment(appointment, null);
                loadAppointments();
            }
        });
        rv.setAdapter(adapter);

        // טעינת המשתמש מFirebase
        User savedUser = SharedPreferencesUtil.getUser(this);
        if (savedUser != null) {
            DatabaseService.getInstance().getUser(savedUser.getId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    currentUser = user;
                    Log.d("AdminActivity", "user loaded: " + user);
                    Log.d("AdminActivity", "adminCategory: " + (user != null ? user.getAdminCategory() : "null"));
                    loadAppointments();
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("AdminActivity", "Failed to load user", e);
                }
            });
        }
    }

    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("התנתקות")
                .setMessage("את/ה בטוח/ה שאת/ה רוצה להתנתק?")
                .setPositiveButton("כן", (dialog, which) -> {
                    SharedPreferencesUtil.signOutUser(this);
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(AdminActivity.this, LandingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("לא", (dialog, which) -> dialog.dismiss())
                .show();
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

    private void loadAppointments() {
        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> allAppointments) {
                Log.d("AdminActivity", "total appointments: " + allAppointments.size());

                List<Appointment> filtered;

                if (currentUser == null ||
                        currentUser.getAdminCategory() == null ||
                        currentUser.getAdminCategory().isEmpty()) {
                    filtered = allAppointments;
                } else {
                    filtered = allAppointments.stream()
                            .filter(a -> a.getCategory() != null &&
                                    a.getCategory().name().equals(currentUser.getAdminCategory()))
                            .collect(Collectors.toList());
                }

                Log.d("AdminActivity", "filtered: " + filtered.size());
                adapter.updateAppointments(filtered);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminActivity", "Failed to load appointments", e);
            }
        });
    }
}