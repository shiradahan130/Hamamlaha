package com.example.hamamlaha.screens;

import android.os.Bundle;
import android.util.Log;
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
import com.example.hamamlaha.models.User;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminActivity extends BaseActivity {

    @Override
    protected boolean hasSideMenu() {
        return true;
    }

    @Override
    protected int getNavMenu() {
        return R.menu.menu_admin;
    }

    private AppointmentAdminAdapter adapter;
    private User currentUser;
    private TextView tvAppointmentCount;

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
                DatabaseService.getInstance().deleteAppointment(appointment.getAppointmentId(), new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void v) {
                        Log.d("AdminActivity", "Appointment deleted successfully");
                        loadAppointments();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e("AdminActivity", "Failed to delete appointment", e);
                    }
                });
            }
        });
        rv.setAdapter(adapter);

        currentUser = SharedPreferencesUtil.getUser(this);
        loadAppointments();

        if (currentUser != null) {
            DatabaseService.getInstance().getUser(currentUser.getId(), new DatabaseService.DatabaseCallback<User>() {
                @Override
                public void onCompleted(User user) {
                    currentUser = user;
                    SharedPreferencesUtil.saveUser(AdminActivity.this, user);
                    loadAppointments();
                }

                @Override
                public void onFailed(Exception e) {
                    Log.e("AdminActivity", "Failed to refresh user", e);
                }
            });
        }
    }

    private void loadAppointments() {
        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> allAppointments) {
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

                adapter.updateAppointments(filtered);
                // ✅ עדכון מספר התורים
                tvAppointmentCount.setText("מספר תורים: " + filtered.size());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminActivity", "Failed to load appointments", e);
            }
        });
    }
}