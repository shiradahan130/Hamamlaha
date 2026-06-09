package com.example.hamamlaha.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.AppointmentAdminAdapter;
import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.models.SalonCategory;
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PendingAppointmentsActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() { return true; }

    @Override
    protected int getNavMenu() { return R.menu.menu_admin; }

    private AppointmentAdminAdapter adapter;
    private User currentUser;
    private TextView tvAppointmentCount, tvEmpty;
    private RecyclerView rv;
    private TabLayout tabLayoutCategory, tabLayoutStatus;

    private List<Appointment> allAppointments = new ArrayList<>();
    private String currentCategory = null;
    private String currentStatus = "PENDING";

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

        tvAppointmentCount = findViewById(R.id.tv_appointment_count);
        tvEmpty = findViewById(R.id.tv_empty);
        rv = findViewById(R.id.rv_appointments);
        tabLayoutCategory = findViewById(R.id.tab_layout_category);
        tabLayoutStatus = findViewById(R.id.tab_layout_status);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AppointmentAdminAdapter(this, new ArrayList<>(), new AppointmentAdminAdapter.OnAppointmentActionListener() {
            @Override// לאשר את התור
            public void onApprove(Appointment appointment) {
                appointment.setStatus("APPROVED");
                DatabaseService.getInstance().createNewAppointment(appointment, null);
                loadAppointments();
            }

            @Override// למחוק תור
            public void onCancel(Appointment appointment) {
                DatabaseService.getInstance().deleteAppointment(appointment.getAppointmentId(), new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void v) {
                        loadAppointments();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("PendingActivity", "Failed to delete appointment", e);
                    }
                });
            }
        });
        rv.setAdapter(adapter);

        currentUser = SharedPreferencesUtil.getUser(this);

        // טאב קטגוריות
        if (currentUser != null &&
                currentUser.getAdminCategory() != null &&
                !currentUser.getAdminCategory().isEmpty()) {
            currentCategory = currentUser.getAdminCategory();
            tabLayoutCategory.setVisibility(View.GONE);
        } else {
            tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText("הכל"));
            for (SalonCategory cat : SalonCategory.values()) {
                tabLayoutCategory.addTab(tabLayoutCategory.newTab().setText(cat.getHebrewName()));
            }
            tabLayoutCategory.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int pos = tab.getPosition();
                    currentCategory = pos == 0 ? null : SalonCategory.values()[pos - 1].name();
                    applyFilterAndDisplay();
                }
                @Override public void onTabUnselected(TabLayout.Tab tab) {}
                @Override public void onTabReselected(TabLayout.Tab tab) {}
            });
        }

        // טאב סטטוס
        tabLayoutStatus.addTab(tabLayoutStatus.newTab().setText("ממתין לאישור"));
        tabLayoutStatus.addTab(tabLayoutStatus.newTab().setText("מאושר"));
        tabLayoutStatus.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentStatus = tab.getPosition() == 0 ? "PENDING" : "APPROVED";
                applyFilterAndDisplay();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        loadAppointments();

        if (currentUser != null) {
            DatabaseService.getInstance().getUser(currentUser.getId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    currentUser = user;
                    SharedPreferencesUtil.saveUser(PendingAppointmentsActivity.this, user);
                    loadAppointments();
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("PendingActivity", "Failed to refresh user", e);
                }
            });
        }
    }

    private void loadAppointments() {
        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> list) {
                allAppointments = list;
                applyFilterAndDisplay();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("PendingActivity", "Failed to load appointments", e);
            }
        });
    }

    private void applyFilterAndDisplay() {
        List<Appointment> filtered = allAppointments.stream()
                .filter(a -> {
                    if (currentCategory != null) {
                        if (a.getCategory() == null) return false;
                        if (!a.getCategory().name().equals(currentCategory)) return false;
                    }
                    return currentStatus.equals(a.getStatus());
                })
                .collect(Collectors.toList());

        adapter.updateAppointments(filtered);
        tvAppointmentCount.setText("מספר תורים: " + filtered.size());

        if (filtered.isEmpty()) {
            rv.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}