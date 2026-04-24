package com.example.hamamlaha.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected boolean hasSideMenu() {
        return false;
    }

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

        Button btnGoBack = findViewById(R.id.btngoback);
        btnGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(Step4Activity.this, Step3Activity.class);
            intent.putExtra("category", category);
            intent.putExtra("options", options);
            intent.putExtra("duration", duration);
            startActivity(intent);
        });

        ImageButton btnInfo = findViewById(R.id.btn_info);
        btnInfo.setOnClickListener(view -> showLegendDialog());

        RecyclerView rv = findViewById(R.id.rv_time_slots);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TimeSlotsAdapter(this, allTimeSlots, bookedSlots, time -> {
            showConfirmationDialog(time);
        });
        rv.setAdapter(adapter);
    }

    private void showLegendDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_legend, null);
        new MaterialAlertDialogBuilder(this)
                .setView(dialogView)
                .setPositiveButton("הבנתי", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String currentUserId = SharedPreferencesUtil.getUserId(this);

        DatabaseService.getInstance().getAppointmentList(new DatabaseService.DatabaseCallback<List<Appointment>>() {
            @Override
            public void onCompleted(List<Appointment> allAppointments) {

                Log.d("DEBUG", "total appointments from DB: " + allAppointments.size());
                Log.d("DEBUG", "looking for date: '" + date + "'");
                for (Appointment a : allAppointments) {
                    Log.d("DEBUG", "appointment -> date: '" + a.getDate() + "' | time: '" + a.getTime() + "' | category: " + a.getCategory());
                }

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
                    int startIndex = allTimeSlots.indexOf(appointment.getTime());
                    int appointmentDuration = appointment.getDuration();

                    Log.d("DEBUG", "appointment time from DB: '" + appointment.getTime() + "', found at index: " + startIndex);

                    if (startIndex >= 0) {
                        for (int i = 0; i < appointmentDuration; i++) {
                            if (startIndex + i < allTimeSlots.size()) {
                                String slot = allTimeSlots.get(startIndex + i);
                                if (!bookedSlots.contains(slot)) {
                                    bookedSlots.add(slot);
                                }
                            }
                        }
                    }
                }

                for (Appointment appointment : userAppointments) {
                    int startIndex = allTimeSlots.indexOf(appointment.getTime());
                    int appointmentDuration = appointment.getDuration();
                    if (startIndex >= 0) {
                        for (int i = 0; i < appointmentDuration; i++) {
                            if (startIndex + i < allTimeSlots.size()) {
                                String slot = allTimeSlots.get(startIndex + i);
                                if (!bookedSlots.contains(slot)) {
                                    bookedSlots.add(slot);
                                }
                            }
                        }
                    }
                }

                List<String> availableSlots = getAvailableSlots();
                List<String> partialSlots = getPartialSlots();

                adapter.updateAvailableSlots(allTimeSlots, availableSlots, partialSlots);

                Log.d("DEBUG", "duration: " + duration);
                Log.d("DEBUG", "booked slots: " + bookedSlots.toString());
                Log.d("DEBUG", "available slots: " + availableSlots.toString());
                Log.d("DEBUG", "partial slots: " + partialSlots.toString());
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("Step4Activity", "Failed to load appointments", e);
            }
        });
    }

    private List<String> getAvailableSlots() {
        List<String> available = new ArrayList<>();
        for (int i = 0; i < allTimeSlots.size(); i++) {
            if (i + duration > allTimeSlots.size()) break;

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

    private List<String> getPartialSlots() {
        List<String> partial = new ArrayList<>();
        for (int i = 0; i < allTimeSlots.size(); i++) {
            String slot = allTimeSlots.get(i);

            if (bookedSlots.contains(slot)) continue;

            if (i + duration > allTimeSlots.size()) {
                partial.add(slot);
                continue;
            }

            boolean hasBlockInRange = false;
            for (int j = 0; j < duration; j++) {
                if (bookedSlots.contains(allTimeSlots.get(i + j))) {
                    hasBlockInRange = true;
                    break;
                }
            }
            if (hasBlockInRange) {
                partial.add(slot);
            }
        }
        return partial;
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
        Appointment appointment = new Appointment(id, date, time, category, currentUserId, "PENDING", duration);
        DatabaseService.getInstance().createNewAppointment(appointment, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void v) {
                Intent intent = new Intent(Step4Activity.this, Step5Activity.class);
                intent.putExtra("category", category);
                intent.putExtra("options", options);
                intent.putExtra("date", date);
                intent.putExtra("time", time);
                intent.putExtra("duration", duration);
                startActivity(intent);
            }

            @Override
            public void onFailed(Exception e) {
                Log.e("Step4Activity", "Failed to create appointment", e);
            }
        });
    }
}