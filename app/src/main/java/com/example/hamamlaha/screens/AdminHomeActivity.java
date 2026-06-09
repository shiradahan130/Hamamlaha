package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hamamlaha.R;
import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.stream.Collectors;

public class AdminHomeActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return true;
    }

    @Override
    protected int getNavMenu() {
        return R.menu.menu_admin;
    }

    private TextView tvUserCount, tvPendingCount, tvAllCount;
    private MaterialCardView cardUsers;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvUserCount    = findViewById(R.id.tv_user_count);
        tvPendingCount = findViewById(R.id.tv_pending_count);
        tvAllCount     = findViewById(R.id.tv_all_count);
        cardUsers      = findViewById(R.id.card_users);

        currentUser = SharedPreferencesUtil.getUser(this);

        // כרטיס לקוחות – מנהל ראשי בלבד
        boolean isMainAdmin = currentUser != null &&
                (currentUser.getAdminCategory() == null ||
                        currentUser.getAdminCategory().isEmpty());

        if (isMainAdmin) {
            cardUsers.setVisibility(View.VISIBLE);
            loadUserCount();
        } else {
            cardUsers.setVisibility(View.GONE);
        }

        // כפתור לרשימת משתמשים
        MaterialButton btnUsers = findViewById(R.id.btn_users);
        btnUsers.setOnClickListener(v ->
                startActivity(new Intent(this, UsersListActivity.class))
        );

        // כפתור לתורים ממתינים
        MaterialButton btnPending = findViewById(R.id.btn_pending);
        btnPending.setOnClickListener(v ->
                startActivity(new Intent(this, PendingAppointmentsActivity.class))
        );

        // כפתור לכל התורים
        MaterialButton btnAll = findViewById(R.id.btn_all_appointments);
        btnAll.setOnClickListener(v ->
                startActivity(new Intent(this, AdminActivity.class))
        );

        loadAppointmentCounts();
    }

    //סופר את המשתמשים מהדטה בייס
    private void loadUserCount() {
        DatabaseService.getInstance().getUserList(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                tvUserCount.setText(String.valueOf(users.size()));
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminHomeActivity", "Failed to load users", e);
            }
        });
    }

    //סופר את כל התורים מהדטה בייס
    private void loadAppointmentCounts() {
        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> allAppointments) {

                boolean isMainAdmin = currentUser == null ||
                        currentUser.getAdminCategory() == null ||
                        currentUser.getAdminCategory().isEmpty();

                //  מסנן רשימת תורים לפי סוג המנהל
                List<Appointment> relevant;
                if (isMainAdmin) {
                    relevant = allAppointments;
                } else {
                    relevant = allAppointments.stream()
                            .filter(a -> a.getCategory() != null &&
                                    a.getCategory().name().equals(currentUser.getAdminCategory()))
                            .collect(Collectors.toList());
                }

                // תורים ממתינים
                long pendingCount = relevant.stream()
                        .filter(a -> "PENDING".equals(a.getStatus()))
                        .count();

                // כל התורים
                long allCount = relevant.size();

                tvPendingCount.setText(String.valueOf(pendingCount));
                tvAllCount.setText(String.valueOf(allCount));
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminHomeActivity", "Failed to load appointments", e);
            }
        });
    }
}