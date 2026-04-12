package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;

import com.example.hamamlaha.R;
import com.example.hamamlaha.adapter.TimeSlotsAdapter;
import com.example.hamamlaha.models.Appointment;
import com.example.hamamlaha.models.SalonCategory;
import com.example.hamamlaha.service.DatabaseService;
import com.example.hamamlaha.utils.SharedPreferencesUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Step4Activity extends BaseActivity {

    String options, date;
    SalonCategory category;
    int duration;

    private TimeSlotsAdapter adapter;
    private List<String> bookedSlots = new ArrayList<>();

    private final List<String> allTimeSlots = Arrays.asList(
            "7:00", "8:00", "9:00", "10:00", "11:00",
            "12:00", "13:00", "14:00", "15:00", "16:00",
            "17:00", "18:00", "19:00", "20:00"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        category = getIntent().getSerializableExtra("category", SalonCategory.class);
        options = getIntent().getStringExtra("options");
        date = getIntent().getStringExtra("date");
        duration = getIntent().getIntExtra("duration", 1);

        TextView tvDate = findViewById(R.id.tv_selected_date);
        tvDate.setText(date);

        RecyclerView rv = findViewById(R.id.rv_time_slots);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimeSlotsAdapter(this, allTimeSlots, bookedSlots, time -> {
            showConfirmationDialog(time);
        });
        rv.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        String currentUserId = SharedPreferencesUtil.getUserId(this);

        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> allAppointments) {

                List<Appointment> categoryAppointments = allAppointments.stream()
                        .filter(appointment ->
                                appointment.getDate().equals(date) &&
                                        (category == null || appointment.getCategory().equals(category)))
                        .collect(Collectors.toList());

                List<Appointment> userAppointments = allAppointments.stream()
                        .filter(appointment ->
                                appointment.getDate().equals(date) &&
                                        appointment.getUserId().equals(currentUserId))
                        .collect(Collectors.toList());

                bookedSlots.clear();

                for (Appointment appointment : categoryAppointments) {
                    bookedSlots.add(appointment.getTime());
                }

                for (Appointment appointment : userAppointments) {
                    if (!bookedSlots.contains(appointment.getTime())) {
                        bookedSlots.add(appointment.getTime());
                    }
                }

                List<String> availableSlots = getAvailableSlots();
                adapter.updateAvailableSlots(allTimeSlots, availableSlots);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("Step4Activity", "Failed to load appointments", e);
            }
        });
    }

    private List<String> getAvailableSlots() {
        List<String> available = new ArrayList<>();
        for (int i = 0; i <= allTimeSlots.size() - duration; i++) {
            boolean canBook = true;
            for (int j = 0; j < duration; j++) {
                if (bookedSlots.contains(allTimeSlots.get(i + j))) {
                    canBook = false;
                    break;
                }
            }
            if (canBook) available.add(allTimeSlots.get(i));
        }
        return available;
    }

    private void showConfirmationDialog(String time) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("קביעת תור")
                .setMessage("לקבוע תור לתאריך " + date + " בשעה " + time + "?\nשירות: " + options)
                .setPositiveButton("אישור", (dialog, which) -> createAppointment(time))
                .setNegativeButton("ביטול", null)
                .show();
    }

    private void createAppointment(String time) {
        String id = DatabaseService.getInstance().generateAppointmentId();
        String currentUserId = SharedPreferencesUtil.getUserId(this);
        Appointment appointment = new Appointment(id, date, time, category, currentUserId, "PENDING");
        DatabaseService.getInstance().createNewAppointment(appointment, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void v) {
                Intent intent = new Intent(Step4Activity.this, Step5Activity.class);
                intent.putExtra("category", category);
                intent.putExtra("options", options);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                startActivity(intent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("Step4Activity", "Failed to create appointment", e);
            }
        });
    }
}