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
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MyAppointmentsActivity extends BaseActivity {

    private MyAppointmentsAdapter adapter;
    private TextView tvCount, tvEmpty;
    private RecyclerView rv;

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

        tvCount = findViewById(R.id.tv_appointment_count);
        tvEmpty = findViewById(R.id.tv_empty);
        rv = findViewById(R.id.rv_my_appointments);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAppointmentsAdapter(this, new ArrayList<>());
        rv.setAdapter(adapter);

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
                // מסנן רק את התורים של המשתמש הנוכחי
                List<Appointment> myAppointments = allAppointments.stream()
                        .filter(a -> a.getUserId() != null && a.getUserId().equals(currentUserId))
                        .collect(Collectors.toList());

                adapter.updateAppointments(myAppointments);
                tvCount.setText("מספר תורים: " + myAppointments.size());

                // מציג הודעה אם אין תורים
                if (myAppointments.isEmpty()) {
                    rv.setVisibility(View.GONE);
                    tvEmpty.setVisibility(View.VISIBLE);
                } else {
                    rv.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailed(Exception e) {
            }
        });
    }
}