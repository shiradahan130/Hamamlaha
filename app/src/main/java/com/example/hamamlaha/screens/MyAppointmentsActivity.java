package com.example.hamamlaha.screens;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.MyAppointmentsAdapter;
import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.models.SalonCategory;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyAppointmentsActivity extends BaseActivity {

    private MyAppointmentsAdapter adapter;
    private TextView tvCount, tvEmpty;
    private RecyclerView rv;
    private TabLayout tabLayoutCategory, tabLayoutStatus;

    private List<Appointment> allMyAppointments = new ArrayList<>();
    private String currentCategory = null; // null = הכל
    private String currentStatus = "PENDING";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_appointments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvCount  = findViewById(R.id.tv_appointment_count);
        tvEmpty  = findViewById(R.id.tv_empty);
        rv       = findViewById(R.id.rv_my_appointments);
        tabLayoutCategory = findViewById(R.id.tab_layout_category);
        tabLayoutStatus   = findViewById(R.id.tab_layout_status);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAppointmentsAdapter(this, new ArrayList<>());
        rv.setAdapter(adapter);

        // טאב קטגוריות
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

        loadMyAppointments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyAppointments();
    }

    private void loadMyAppointments() {
        String currentUserId = SharedPreferencesUtil.getUserId(this);

        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> allAppointments) {
                allMyAppointments = allAppointments.stream()
                        .filter(a -> a.getUserId() != null && a.getUserId().equals(currentUserId))
                        .collect(Collectors.toList());

                tvCount.setText("מספר תורים: " + allMyAppointments.size());
                applyFilterAndDisplay();
            }

            @Override
            public void onFailed(Exception e) {}
        });
    }

    private void applyFilterAndDisplay() {
        List<Appointment> filtered = allMyAppointments.stream()
                .filter(a -> {
                    if (currentCategory != null) {
                        if (a.getCategory() == null) return false;
                        if (!a.getCategory().name().equals(currentCategory)) return false;
                    }
                    return currentStatus.equals(a.getStatus());
                })
                .collect(Collectors.toList());

        adapter.updateAppointments(filtered);
        tvCount.setText("מספר תורים: " + filtered.size());

        if (filtered.isEmpty()) {
            rv.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            rv.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
        }
    }
}