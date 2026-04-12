package com.example.hamamlaha.screens;

import android.os.Bundle;
import android.util.Log;

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

    private AppointmentAdminAdapter adapter;
    private User currentUser;

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

        String userId = SharedPreferencesUtil.getUserId(this);
        DatabaseService.getInstance().getUser(userId, new DatabaseService.DatabaseCallback<User>() {
            @Override
            public void onCompleted(User user) {
                currentUser = user;
                loadAppointments();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminActivity", "Failed to load user", e);
            }
        });
    }

    private void loadAppointments() {
        Log.d("AdminActivity", "currentUser: " + currentUser);
        if (currentUser != null) {
            Log.d("AdminActivity", "adminCategory: " + currentUser.getAdminCategory());
        }

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

                Log.d("AdminActivity", "filtered appointments: " + filtered.size());
                adapter.updateAppointments(filtered);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("AdminActivity", "Failed to load appointments", e);
            }
        });
    }
}